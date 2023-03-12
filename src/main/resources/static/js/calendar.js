$(document).ready(function(){
    var yearInput = document.getElementById('year-input');
    var prevYearBtn = document.getElementById('prev-year-btn');
    var nextYearBtn = document.getElementById('next-year-btn');

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