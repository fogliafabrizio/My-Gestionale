$(document).ready(function(){
    let url = "/api/user/"

    $('.custom-btn').on("click", function(){
        /*  Svuota card */
        $('#info-body').empty();
        $('#info-title').empty();
        /*
          Creazione URL per fare richiesta POST
          */
        let type_data = $(this).val();


        /*  Chiamata POST */
        switch(type_data){

        case "personal_data":
            $('#info-title').append("<h5><i class='fa-solid fa-user'></i> Dati Personali</h5>");
            bodyPersonalData(type_data);
            break;
        case "change_password":
            $('#info-title').append("<h5><i class='fa-solid fa-lock'></i> Cambio Password</h5>");
            bodyChangePassword(type_data);
            break;
        }

    });

    function bodyPersonalData(type_data){
        let id = $('#id').val()
        let url_req = url + type_data + "/" + id;

        $.ajax({
            url: url_req,
            type: 'POST',
            success: function(data) {

            //  Se compleanno non impostato
            if(data['dateOfBirthday'] == null){
                var compleanno = "Non definita";
            } else {
                // Crea un nuovo oggetto Date() a partire dalla stringa
                var dob = new Date(data['dateOfBirthday']);

                // Estrai giorno, mese e anno dalla data
                var giorno = dob.getDate();
                var mese = dob.getMonth() + 1; // JavaScript conta i mesi a partire da 0
                var anno = dob.getFullYear();

                // Formatta la data come una stringa nel formato dd/mm/yyyy
                var compleanno = giorno + '/' + mese + '/' + anno;

            }

            $('#info-body').append(
              '<div class="row">' +
                '<div class="col-sm-3 text-muted font-weight-bold">Nome:</div>' +
                '<div class="col-sm-9">' + data['firstName'] + '</div>' +
              '</div>' +
              '<div class="row">' +
                '<div class="col-sm-3 text-muted font-weight-bold">Cognome:</div>' +
                '<div class="col-sm-9">' + data['lastName'] + '</div>' +
              '</div>' +
              '<div class="row">' +
                '<div class="col-sm-3 text-muted font-weight-bold">Email:</div>' +
                '<div class="col-sm-9">' + data['email'] + '</div>' +
              '</div>' +
              '<div class="row">' +
                '<div class="col-sm-3 text-muted font-weight-bold">Data di nascita:</div>' +
                '<div class="col-sm-9">' + compleanno + '</div>' +
              '</div>' +
              '<div class="row">' +
                  '<div class="col-sm-3 text-muted font-weight-bold">Ruolo:</div>' +
                  '<div class="col-sm-9">' + data['role'] + '</div>' +
                '</div>'
            );

            },
            error: function(jqXHR, textStatus, errorThrown) {
              console.error(errorThrown);
            }
          });
    }

    function bodyChangePassword(type_data){
        let id = $('#id').val()
        let url_req = url + type_data + "/" + id;

        $("#info-body").append(
                    '<h5 class="card-title text-center">Reset Password</h5>' +
                    '<p>Per cambiare la password, inserisci la nuova password nei riquadri sottostanti.</p>' +
                    '<p>La password deve rispettare queste regole:</p>' +
                    '<ul>' +
                    '<li>Deve essere lunga almeno 8 caratteri</li>' +
                    '<li>Deve contenere almeno un carattere numerico</li>' +
                    '<li>Deve avere almeno un carattere minuscolo e uno MAIUSCOLO</li>' +
                    '<li>Deve contenere almeno un carattere speciale (!@#$%^&amp;*)</li>' +
                    '<li>Non deve contenere spazi bianchi</li>' +
                    '</ul>' +
                    '<div id="password-form">' +
                    '<form id="formChangePassword" action="' + url_req + '" method="post">' +
                    '<div class="mb-3">' +
                    '<label for="inputOldPassword" class="form-label">Vecchia password</label>' +
                    '<input type="password" class="form-control" id="inputOldPassword" name="oldPassword" placeholder="Inserisci la vecchia password" required>' +
                    '</div>' +
                    '<div class="mb-3">' +
                    '<label for="inputNewPassword" class="form-label">Nuova password</label>' +
                    '<input type="password" class="form-control" id="inputNewPassword" name="newPassword" placeholder="Inserisci la nuova password" required>' +
                    '</div>' +
                    '<div class="mb-3">' +
                    '<label for="inputConfermaPassword" class="form-label">Conferma password</label>' +
                    '<input type="password" class="form-control" id="inputConfermaPassword" name="confirmPassword" placeholder="Ripeti la password" required>' +
                    '</div>' +
                    '<button type="submit" class="btn btn-primary">Cambia Password</button>' +
                    '</form>' +
                    '</div>'
                );

        $("#formChangePassword").submit(function(e) {
          e.preventDefault(); // Previeni il comportamento di default della form

            $(".alert").remove();
          $.ajax({
            url: $(this).attr('action'),
            method: $(this).attr('method'),
            data: $(this).serialize(),
            success: function(response) {

              if (response === "OLD_PSW_NOT_OK") {
              // Messaggio di errore
                    $("#password-form").append('<div class="alert alert-danger mt-2 col-7 text-center mx-auto" role="alert"><span>La vecchia password inserita non è corretta.</span></div>');
                } else if(response === "NOT_MATCH_PSW"){
                    $("#password-form").append('<div class="alert alert-danger mt-2 col-7 text-center mx-auto" role="alert"><span>Le due password non corrispondono</span></div>');
                } else if (response === "NOT_REGEX_PSW"){
                    $("#password-form").append('<div class="alert alert-danger mt-2 col-7 text-center mx-auto" role="alert"><span>La password non rispetta le regole!</span></div>');
                } else {
                    // Messaggio di successo
                    $("#password-form").html('<div class="alert alert-success col-7 text-center mx-auto" role="alert"><span>Password cambiata con successo!</span></div>');
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
              console.error(errorThrown);
            }
          });
          });

    }
});