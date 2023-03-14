$(document).ready(function(){

    $('#new-group-form').submit(function(event) {

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

     // ottieni i dati del form e trasformali in un oggetto JSON
         var formData = {
             groupName: $('#group-name').val(),
             groupDescription: $('#group-description').val(),
             groupVisibility: $('#group-visibility').val(),
             userIds: userIds
         };

         console.log($('#group-visibility').val());

         // effettua la chiamata ajax per creare l'evento
         $.ajax({
             url: '/api/group/createGroup',
             type: 'POST',
             data: JSON.stringify(formData),
             contentType: 'application/json',
             success: function(response) {
                 // gestisci la risposta dal server
                 if (response === 'OK') {
                // Effettua il redirect
                window.location.href = '/groups';

                } else if (response === "NAME_TAKEN") {
                     // mostra un messaggio di errore
                     // TODO: Sistema Js Calendar - Success non più necessario - neanche BUTTON dopo
                     $("#saveMessage").addClass("alert alert-danger").text("Esiste già un gruppo con questo nome.. sarebbe meglio cambiarlo.");
                 }
             },
             error: function(error) {
                 // gestisci l'errore dal server
                 console.log(error);
             }
         });
    });

});