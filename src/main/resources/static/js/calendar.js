$(document).ready(function(){
    var yearInput = document.getElementById('year-input');
    var prevYearBtn = document.getElementById('prev-year-btn');
    var nextYearBtn = document.getElementById('next-year-btn');

    //  FORM CREA EVENTO

    // selezionare gli elementi da disattivare
    const $startTimeInput = $('#event-start-time');
    const $endTimeInput = $('#event-end-time');
    const $allDayCheckbox = $('#all-day-event');

    // impostare il valore predefinito all'apertura della pagina
    if ($allDayCheckbox.prop('checked')) {
      $startTimeInput.prop('disabled', true).val('00:00');
      $endTimeInput.prop('disabled', true).val('23:59');
    }

    // rilevare il cambiamento nella casella di controllo
    $allDayCheckbox.on('change', (event) => {
      if ($(event.target).prop('checked')) {
        // disabilita i due input di tempo e impostare il valore a '00:00' e '23:59'
        $startTimeInput.prop('disabled', true).val('00:00');
        $endTimeInput.prop('disabled', true).val('23:59');
      } else {
        // abilita i due input di tempo
        $startTimeInput.prop('disabled', false);
        $endTimeInput.prop('disabled', false);
      }
    });

    // Selezione del checkbox "Tutti gli utenti"
      $('#allUsers').click(function() {
        if($(this).is(':checked')) {
          $('input[name^="user-"]').prop('checked', true);
        } else {
          $('input[name^="user-"]').prop('checked', false);
        }
      });

      // Selezione/deselezione di tutti gli utenti
      $('input[name^="user-"]').click(function() {
        if($('input[name^="user-"]').length === $('input[name^="user-"]:checked').length) {
          $('#allUsers').prop('checked', true);
        } else {
          $('#allUsers').prop('checked', false);
        }
      });

    const allGroupsCheckbox = $('#allGroups');
      const groupCheckboxes = $('input[name^="group-"]');

      // Aggiungere l'event listener per il checkbox 'Tutti i gruppi'
      allGroupsCheckbox.on('change', function() {
        // Selezionare o deselezionare tutti i checkbox di gruppo
        groupCheckboxes.prop('checked', this.checked);
      });

      // Aggiungere un event listener per i checkbox di gruppo individuali
      groupCheckboxes.on('change', function() {
        // Se ci sono checkbox non selezionati, deselezionare 'Tutti i gruppi'
        if (groupCheckboxes.filter(':not(:checked)').length) {
          allGroupsCheckbox.prop('checked', false);
        } else {
          allGroupsCheckbox.prop('checked', true);
        }
      });

      // seleziona il form e ascolta l'evento submit
        $('#new-event-form').submit(function(event) {
          // evita il submit di default del form
          event.preventDefault();

          var idOwner = $("#id").val();
          console.log(idOwner);

          // ottieni tutti i checkbox degli utenti
          var userCheckboxes = $('input[id^="user-"]');

          // crea un array vuoto per gli ID utenti selezionati
          var userIds = [];

          // per ogni checkbox selezionato, aggiungi l'ID all'array
          userCheckboxes.filter(':checked').each(function() {
              userIds.push($(this).val());
          });

          // ottieni tutti i checkbox dei gruppi
          var groupCheckboxes = $('input[id^="group-"]');

          // crea un array vuoto per gli ID gruppi selezionati
          var groupIds = [];

          // per ogni checkbox selezionato, aggiungi l'ID all'array
          groupCheckboxes.filter(':checked').each(function() {
              groupIds.push($(this).val());
          });

         // ottieni i dati del form e trasformali in un oggetto JSON
             var formData = {
                 eventName: $('#event-name').val(),
                 eventDescription: $('#event-description').val(),
                 eventLocation: $('#event-location').val(),
                 eventVisibility: $('#event-visibility').val(),
                 eventDate: $('#event-date').val(),
                 eventStartTime: $('#event-start-time').val(),
                 eventEndTime: $('#event-end-time').val(),
                 allDayEvent: $('#all-day-event').prop('checked'),
                 userIds: userIds,
                 groupIds: groupIds
             };

             console.log(formData);

             // effettua la chiamata ajax per creare l'evento
             $.ajax({
                 url: '/api/calendar/createEvent/' + idOwner,
                 type: 'POST',
                 data: JSON.stringify(formData),
                 contentType: 'application/json',
                 success: function(response) {

                     // gestisci la risposta dal server
                     console.log(response);
                 },
                 error: function(error) {
                     // gestisci l'errore dal server
                     console.log(error);
                 }
             });
        });


    prevYearBtn.addEventListener('click', function() {
      yearInput.value = parseInt(yearInput.value) - 1;
    });

    nextYearBtn.addEventListener('click', function() {
      yearInput.value = parseInt(yearInput.value) + 1;
    });

    var today = new Date();
    var currentMonth = today.getMonth() + 1; //+1 perche' i mesi partono da 0
    var currentYear = today.getFullYear();
    var currentDay = today.getDate();
    // Seleziona l'elemento dell'elenco corrispondente al mese corrente e aggiungi la classe "active"
    // Seleziona l'elemento della lista dei mesi con lo stesso valore di currentMonth e aggiungi la classe "active"
    $(".nav-link").filter("[data-value='" + currentMonth + "']").addClass("fw-bold");

    //  Prima generazione - Mese e Anno corrente
    generateCalendar(currentYear, currentMonth);
    generateEvents(currentDay, currentMonth, currentYear);

    var yearSelected = currentYear;
    var monthSelected = currentMonth;
    //  Cambio anno
    $('#next-year-btn').on("click", function(){
        yearSelected++;
        generateCalendar(yearSelected, monthSelected);
    });
    $('#prev-year-btn').on("click", function(){
        yearSelected--;
        generateCalendar(yearSelected, monthSelected);
    });

    //  Cambio mese
    $(".month").on("click", function(){
        monthSelected = $(this).data("value");
        generateCalendar(yearSelected, monthSelected);
    });

    function generateCalendar(year, month) {
      month = month - 1;
      // Ottieni il numero di giorni del mese e l'indice del giorno della settimana in cui inizia il mese
      var daysInMonth = new Date(year, month + 1, 0).getDate();
      var firstDayOfWeek = new Date(year, month, 1).getDay() - 1;
      if (firstDayOfWeek === -1) {
        firstDayOfWeek = 6; // se il primo giorno della settimana è domenica, considera il sabato come il settimo giorno della settimana
      }
      console.log(daysInMonth);
      console.log(firstDayOfWeek);

      // Seleziona l'elemento tbody della tabella del calendario e rimuovi tutte le righe
      var tbody = $("#calendar-table tbody");
      tbody.empty();

      // Aggiungi le righe della tabella per le settimane del mese
      var date = 1;
      for (var i = 0; i < 6; i++) {
        var row = $("<tr></tr>");
        for (var j = 0; j < 7; j++) {
          var cell = $("<td></td>");
          if (i === 0 && j < firstDayOfWeek) {
            // Aggiungi celle vuote per i giorni precedenti il primo giorno del mese
            cell.text("");
          } else if (date > daysInMonth) {
            // Aggiungi celle vuote per i giorni successivi all'ultimo giorno del mese
            cell.text("");
          } else {
            // Aggiungi il numero del giorno nella cella corrispondente
            cell.text(date);
            date++;
          }
          row.append(cell);
        }
        tbody.append(row);
      }
      // Verifica se la settimana corrente ha celle vuote alla fine e elimina la riga vuota
        if (tbody.find("tr:last td:empty").length === 7 - (daysInMonth - date + 1) % 7) {
          tbody.find("tr:last").remove();
        }
      // Mette titolo alla tabella
      var monthTitle = "";
      var checkMonth = month + 1;
      switch(checkMonth){
        case 1:
            monthTitle = "Gennaio";
            break;
        case 2:
            monthTitle = "Febbraio";
            break;
        case 3:
            monthTitle = "Marzo";
            break;
        case 4:
            monthTitle = "Aprile";
            break;
        case 5:
            monthTitle = "Maggio";
            break;
        case 6:
            monthTitle = "Giugno";
            break;
        case 7:
            monthTitle = "Luglio";
            break;
        case 8:
            monthTitle = "Agosto";
            break;
        case 9:
            monthTitle = "Settembre";
            break;
        case 10:
            monthTitle = "Ottobre";
            break;
        case 11:
            monthTitle = "Novembre";
            break;
        case 12:
            monthTitle = "Dicembre";
            break;
      }
      $("#monthYear").html(monthTitle + " " + year);
      if(year === currentYear && month + 1 === currentMonth){
        var currentCell = tbody.find("td:contains('" + currentDay + "')");
        // Aggiungi la classe "fw-bold" alla cella corrente per evidenziarla
        currentCell.addClass("fw-bold text-primary");
      }
    }

    function generateEvents(dayEv, monthEv, yearEv){
        var postData = {
          day : dayEv,
          month: monthEv,
          year: yearEv
        };

        // esegui la chiamata AJAX
        $.ajax({
          url: "/api/calendar/getEvents",
          type: "POST",
          data: postData,
          success: function(response) {
            // gestisci la risposta della API
            console.log(response);
          },
          error: function(xhr, status, error) {
            // gestisci gli errori della chiamata
            console.error(error);
          }
        });
    }




});