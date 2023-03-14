$(document).ready(function(){
    var idEvent = $("#id").val();

    //  GESTIONE MODAL

    //  1   -   SE CHECKBOX ALL DAY --> INPUT DISABLED
    const $allDayCheckbox = $('#all-day-event');
    const $startTimeInput = $('#event-start-time');
        const $endTimeInput = $('#event-end-time');
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

     //  2   -   CHECKBOX ALL USER E ALL GROUP
     if($("#allUsers").prop('checked')){
        var select = $('#event-visibility'); // seleziona la select
        $('input[name^="user-"]').prop('checked', true);
        select.val('PUBLIC'); // disabilita la select impostando il valore su "PUBLIC"
        select.prop('disabled', true);
     }

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
    const allGroupsChecked = groupCheckboxes.toArray().every(function(checkbox) {
        return $(checkbox).is(':checked');
      });
      if (allGroupsChecked) {
        allGroupsCheckbox.prop('checked', true);
      } else {
        allGroupsCheckbox.prop('checked', false);
      }

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
                       url: '/api/calendar/modifyEvent/' + idEvent,
                       type: 'POST',
                       data: JSON.stringify(formData),
                       contentType: 'application/json',
                       success: function(response) {
                           // gestisci la risposta dal server
                           if (response === 'OK') {
                               // Effettua il redirect
                               window.location.href = '/calendar/event/' + idEvent;

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
      // Seleziona il pulsante di cancellazione dell'evento
      const deleteButton = $('#deleteButton');

      // Aggiungi un gestore per l'evento di clic del pulsante di cancellazione
      deleteButton.on('click', function() {
        // Invia la richiesta POST al tuo controller Spring Boot
        $.ajax({
          type: 'POST',
          url: '/api/calendar/deleteEvent/' + idEvent, // Sostituisci eventId con l'ID dell'evento
          success: function(response) {
            // Se la richiesta ha successo, chiudi il modale e ricarica la pagina
            window.location.href = '/calendar';
          },
          error: function(xhr) {
            // Se la richiesta fallisce, visualizza un messaggio di errore
            alert('Impossibile cancellare l\'evento');
          }
        });
      });

    $("#pdfButton").click(function() {
      // Definisci le font da utilizzare nel documento PDF
      pdfMake.fonts = {
        Roboto: {
          normal: 'https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/fonts/Roboto/Roboto-Regular.ttf',
          bold: 'https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/fonts/Roboto/Roboto-Medium.ttf',
          italics: 'https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/fonts/Roboto/Roboto-Italic.ttf',
          bolditalics: 'https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/fonts/Roboto/Roboto-MediumItalic.ttf'
        }
      };

      // Seleziona la tabella e ottieni l'HTML
      const table = document.querySelector("table");
      const html = table.outerHTML;
      const title = $("#eventName").text();

      // Crea il documento PDF
      const docDefinition = {
              content: [
                  {
                      text: title,
                      style: "header"
                  },
                  {
                      text: " ",
                      margin: [0, 0, 0, 0]
                  },
                  {
                      table: {
                              headerRows: 1,
                              widths: ["*", "*", "*", "*"],
                              body: []
                            },
                            style: "tableStyle" // Utilizza lo stile "tableStyle" per la tabella
                          }
              ],
              styles: {
                  header: {
                      fontSize: 18,
                      bold: true,
                      margin: [0, 0, 0, 20],
                      color: "#007bff"
                  },
                  tableHeader: {
                      bold: true,
                      fontSize: 13,
                      color: "#007bff"
                  },
                  tableStyle: {
                      margin: [0, 5, 0, 5],
                      border: [false, false, false, false],
                      header: {
                        bold: true,
                        fontSize: 13,
                        color: "#007bff",
                        fillColor: "#f5f5f5",
                        margin: [5, 5, 5, 5],
                        border: [true, true, true, false]
                      },
                      tableCellBold: {
                        bold: true,
                        fillColor: "#f5f5f5",
                        margin: [5, 5, 5, 5],
                        border: [true, false, false, true]
                      },
                      tableCell: {
                        margin: [5, 5, 5, 5],
                        border: [false, false, false, true]
                      }
                    }
              }
          };

      // Converte la tabella HTML in un array di righe e colonne
      const tableRows = table.rows;
      const tableData = [];

      // Itera attraverso tutte le righe della tabella
      for (let i = 0; i < tableRows.length; i++) {
        const tableCells = tableRows[i].cells;
        const rowData = [];

        // Itera attraverso tutte le colonne della riga corrente
        for (let j = 0; j < tableCells.length; j++) {
          rowData.push(tableCells[j].textContent.trim());
        }

        // Aggiungi la riga alla matrice dei dati della tabella
        tableData.push(rowData);
      }

      // Aggiungi i dati della tabella al documento PDF
      docDefinition.content[2].table.body = tableData;

      // Crea il PDF
      pdfMake.createPdf(docDefinition).download("table.pdf");

      //  TODO: GENERARE PDF PIU BELLO!
    });


});