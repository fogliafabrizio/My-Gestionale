$(document).ready(function(){
    var yearInput = document.getElementById('year-input');
    var prevYearBtn = document.getElementById('prev-year-btn');
    var nextYearBtn = document.getElementById('next-year-btn');
    var idOwner = $("#id").val();

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
        var select = $('#event-visibility'); // seleziona la select
        if($(this).is(':checked')) {
        select.val('PUBLIC'); // disabilita la select impostando il valore su "PUBLIC"
        select.prop('disabled', true);
          $('input[name^="user-"]').prop('checked', true);
        } else {
        select.prop('disabled', false);
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
                 eventLink: $('#event-link').val(),
                 eventVisibility: $('#event-visibility').val(),
                 eventDate: $('#event-date').val(),
                 eventStartTime: $('#event-start-time').val(),
                 eventEndTime: $('#event-end-time').val(),
                 allDayEvent: $('#all-day-event').prop('checked'),
                 allUsers : $('#allUsers').prop('checked'),
                 userIds: userIds,
                 groupIds: groupIds
             };

             // effettua la chiamata ajax per creare l'evento
             $.ajax({
                 url: '/api/calendar/createEvent/' + idOwner,
                 type: 'POST',
                 data: JSON.stringify(formData),
                 contentType: 'application/json',
                 success: function(response) {
                     // gestisci la risposta dal server
                     if (response === "OK") {
                             // cancella i valori del modal
                             $("#event-name").val("");
                             $("#event-description").val("");
                             $("#event-location").val("");
                             $("#event-visibility").val("");
                             $("#event-date").val("");
                             $("#event-start-time").val("");
                             $("#event-end-time").val("");
                             $("#all-day-event").prop("checked", false);
                             $("input[name^='user-']").prop("checked", false);
                             $("input[name^='group-']").prop("checked", false);
                             $("#allUsers").prop("checked", false);
                             $("#allGroups").prop("checked", false);
                             $startTimeInput.prop('disabled', false);
                             $endTimeInput.prop('disabled', false);

                             // mostra un messaggio di successo
                             $("#saveMessage").removeClass("alert alert-danger");
                             $("#saveMessage").addClass("alert alert-success").text("Evento creato con successo.");
                         } else if (response === "BEFORE_TODAY") {
                             // mostra un messaggio di errore
                             $("#saveMessage").removeClass("alert alert-success");
                             $("#saveMessage").addClass("alert alert-danger").text("La data dell'evento non può essere precedente a oggi.");
                         } else if (response === "ERR_TIME") {
                            $("#saveMessage").removeClass("alert alert-success");
                            $("#saveMessage").addClass("alert alert-danger").text("L'ora di inizio deve essere precedente all'orario di fine");
                         }
                         $("#btnNewEvent").on("click", function(){
                            $("#saveMessage").removeClass("alert alert-success alert-danger");
                            $("#saveMessage").empty();
                         });
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

      /*
            colora caselle con eventi:
            -   CERCO EVENTI IN MESE E ANNO
            -   RITORNA EVENTI
            -   TODO: colorare caselle giorni
      */
      var postDataAll = {
      month : month + 1,
      year : year
      }
      $.ajax({
            url: "/api/calendar/getAllEvents/" + idOwner,
            type: "POST",
            data: postDataAll,
            success: function(response) {
            console.log(response);
              // gestisci la risposta della API
              $.each(response, function(index, event) {
                // estrai il giorno dalla data dell'evento usando moment.js
                var day = moment(event.date).format("DD");
                if (day.startsWith('0')) {
                  day = day.substring(1);
                }
                // cerca la cella corrispondente nella tabella usando jQuery
                var cell = $('td').filter(function() {
                        return $(this).text() === day;
                    });
                // esegui qualche operazione sulla cella, ad esempio aggiungi una classe
                cell.addClass('bg-warning-subtle');
              });
            },
            error: function(xhr, status, error) {
              // gestisci gli errori della chiamata
              console.error(error);
            }
          });

      // Aggiungi un evento click a tutte le celle del calendario
          $("#calendar-table td").click(function() {
            // Verifica se la cella cliccata ha un valore numerico
            if ($.isNumeric($(this).text())) {
              // Se la cella ha un valore numerico, chiama la funzione GenerateEvents con il giorno selezionato come argomento
              var day = parseInt($(this).text());
              console.log(day);
              generateEvents(day, month + 1, year);
            }
          });
    }

    //  GENERAZIONE EVENTI
    function generateEvents(dayEv, monthEv, yearEv){
        var postData = {
          day : dayEv,
          month: monthEv,
          year: yearEv
        };
        console.log(dayEv +"/" + monthEv + "/" + yearEv);

        $("#currentDate").html(dayEv + "/" + monthEv + "/" + yearEv);
        $("#list-event").empty();

        // esegui la chiamata AJAX
        $.ajax({
          url: "/api/calendar/getEvents/" + idOwner,
          type: "POST",
          data: postData,
          success: function(response) {
            // gestisci la risposta della API

            // crea un elemento HTML per ciascun evento
             if (Array.isArray(response) && response.length > 0) {
             // Ci sono eventi
                $.each(response, function(index, event) {

                if(!event.allDay){
                    var oraInizio = event.beginHour.substring(0, 5);
                    var oraFine = event.endHour.substring(0, 5);
                    var orario = oraInizio + " - " + oraFine;
                } else {
                    var orario = "Tutto il giorno";
                }

                /*
                    QUALI SONO I CASI POSSIBILI?
                    -   ALL DAY OR NOT
                    -   PUBLIC/PRIVATE/FESTIVITY/BIRTHDAY
                */
                if(event.festivity == true){
                    //  FESTIVITA'
                    $("#list-event").append(
                    '<a class="list-group-item bg-success-subtle list-group-item-action" aria-current="true">' +
                         '<div class="d-flex w-100 justify-content-between">' +
                             '<h5 class="mb-1"><i class="fa-sharp fa-solid fa-gift"></i> ' + event.name + '</h5>' +
                         '</div>' +
                         '<p class="mb-1">' + event.description + '</p>' +
                     '</a>');
                } else if (event.visibility == 'PRIVATE') {
                    //  PRIVATE
                    $("#list-event").append(
                    '<a href="/calendar/event/' + event.id + '"  class="list-group-item list-group-item-action" aria-current="true">' +
                         '<div class="d-flex w-100 justify-content-between">' +
                             '<h5 class="mb-1"><i class="fa-solid fa-lock"></i> ' + event.name + '</h5>' +
                            ' <small>' + orario + '</small>' +
                         '</div>' +
                         '<small class="fst-italic">' + event.location + '</small>' +
                     '</a>');
                } else if (event.dob == true){
                    //  BIRTHDAY
                    $("#list-event").append(
                    '<a class="list-group-item bg-warning-subtle list-group-item-action" aria-current="true">' +
                         '<div class="d-flex w-100 justify-content-between">' +
                             '<h5 class="mb-1"><i class="fa-solid fa-cake-candles"></i> ' + event.name + '</h5>' +
                         '</div>' +
                         '<p class="mb-1">' + event.description + '</p>' +
                     '</a>');
                } else if (event.visibility == 'PUBLIC') {
                    //  PUBLIC
                    $("#list-event").append(
                    '<a href="/calendar/event/' + event.id + '" class="list-group-item list-group-item-action" aria-current="true">' +
                         '<div class="d-flex w-100 justify-content-between">' +
                             '<h5 class="mb-1"><i class="fa-solid fa-unlock"></i> ' + event.name + '</h5>' +
                            ' <small>' + orario + '</small>' +
                         '</div>' +
                         '<small class="fst-italic">' + event.location + '</small>' +
                     '</a>');
                }

                });
             } else {
                // non ci sono eventi
                $("#list-event").append("<p class='text-center mt-3'> Non ci sono eventi in data " + dayEv + "/" + monthEv + "/" + yearEv + "</p>");
             }

          },
          error: function(xhr, status, error) {
            // gestisci gli errori della chiamata
            console.error(error);
          }
        });
    }




});