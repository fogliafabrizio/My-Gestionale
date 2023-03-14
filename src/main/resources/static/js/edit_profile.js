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
            $('#info-title').append("<span><i class='fa-solid fa-user'></i> Dati Personali</span>");
            bodyPersonalData(type_data);
            break;
        case "change_password":
            $('#info-title').append("<span><i class='fa-solid fa-lock'></i> Cambio Password</span>");
            bodyChangePassword(type_data);
            break;
        case "groups":
             $('#info-title').append("<span><i class='fa-solid fa-user-group'></i> Gruppi</span>");
             bodyGroups("personal_data");
             break;
        case "image_user":
             $('#info-title').append("<span><i class='fa-solid fa-image'></i> Immagine del profilo</span>");
             bodyImageUser(type_data);
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
            console.log(data);

            var created = new Date(data['created_on']);

            var giornoC = created.getDate();
            var mesi = ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"];
            var meseC = mesi[created.getMonth()];
            var annoC = created.getFullYear();
            var oraC = created.getHours();
            var minutiC = created.getMinutes();

            var dataC = giornoC + " " + meseC + " " + annoC + " " + oraC + ":" + minutiC;

            var update = new Date(data['update_on']);

            var giornoU = update.getDate();
            var meseU = mesi[update.getMonth()];
            var annoU = update.getFullYear();
            var oraU = update.getHours();
            var minutiU = update.getMinutes();

            var dataU = giornoU + " " + meseU + " " + annoU + " " + oraU + ":" + minutiU;

            //  Se compleanno non impostato
            if(data['dateOfBirthday'] == null){
                var compleanno = "Non definita";
            } else {
                // Crea un nuovo oggetto Date() a partire dalla stringa
                var dob = new Date(data['dateOfBirthday']);

                // Estrai giorno, mese e anno dalla data
                var giornoDob = dob.getDate();
                var meseDob = dob.getMonth() + 1; // JavaScript conta i mesi a partire da 0
                var annoDob = dob.getFullYear();

                // Formatta la data come una stringa nel formato dd/mm/yyyy
                var compleanno = giornoDob + '/' + meseDob + '/' + annoDob;

            }

            $('#info-body').append(
              '<table class="table">' +
                '<tbody>' +
                  '<tr>' +
                    '<th scope="row">Nome</th>' +
                    '<td>' + data['firstName'] + '</td>' +
                  '</tr>' +
                  '<tr>' +
                    '<th scope="row">Cognome</th>' +
                    '<td>' + data['lastName'] + '</td>' +
                  '</tr>' +
                  '<tr>' +
                    '<th scope="row">Email</th>' +
                    '<td>' + data['email'] + '</td>' +
                  '</tr>' +
                  '<tr>' +
                    '<th scope="row">Data di nascita</th>' +
                    '<td id="dateOfBirthday">' + compleanno + '</td>' +
                  '</tr>' +
                  '<tr>' +
                    '<th scope="row">Ruolo</th>' +
                    '<td>' + data['role'] + '</td>' +
                  '</tr>' +
                  '<tr>' +
                    '<th scope="row">Creato il</th>' +
                    '<td>' + dataC + '</td>' +
                  '</tr>' +
                  '<tr>' +
                    '<th scope="row">Ultimo aggiornamento</th>' +
                    '<td>' + dataU + '</td>' +
                  '</tr>' +
                '</tbody>' +
              '</table>' +
              '<div class="row pt-2">' +
              '<div class="col">' +
              '<!-- Button trigger modal -->' +
               '<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#birthdateModal">'+
                 'Modifica data di nascita' +
               '</button>' +

             '  <!-- Modal -->' +
               '<div class="modal fade" id="birthdateModal" tabindex="-1" aria-labelledby="birthdateModalLabel" aria-hidden="true">' +
                 '<div class="modal-dialog">' +
                   '<div class="modal-content">' +
                     '<div class="modal-header">' +
                       '<h1 class="modal-title fs-5" id="birthdateModalLabel">Data di Nascita</h1>' +
                       '<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' +
                     '</div>' +
                    '<form id="formBirthdate" action="' + url + type_data + '/birthdate/' + id + '" method="post">' +
                     '<div class="modal-body">' +
                        '<label for="birthdate" class="form-label">Seleziona una data</label>' +
                       '<input type="text" class="form-control" id="birthdate" name="birthdate" value="' + compleanno + '">' +
                     '</div>' +
                     '<div class="modal-footer">' +
                    '<button type="button" class="btn btn-secondary" id="resetBirthdate">Ripristina data di nascita</button>' +
                    '<button type="submit" class="btn btn-primary">Cambia data di nascita</button>' +
                     '</div>' +
                     '</form>'+
                   '</div>' +
                 '</div>' +
               '</div>' +
               '</div>' +
               '</div>'
            );
            $("#resetBirthdate").on("click", function(){
                $("#birthdate").val("");
            });
            $("#formBirthdate").submit(function(e) {
              e.preventDefault(); // Previeni il comportamento di default della form

                $(".alert").remove();
              $.ajax({
                url: $(this).attr('action'),
                method: $(this).attr('method'),
                data: $(this).serialize(),
                success: function(response) {

                  if (response === "BIRTH_RESET") {
                  // Messaggio di errore
                       $("#dateOfBirthday").html("Non definita");
                       $("#formBirthdate").append('<div class="alert alert-success col-9 mt-2 text-center mx-auto" role="alert"><span>Data di nascita ripristinata con successo!</span></div>');
                  } else if (response === "BIRTH_OK"){
                        var newBirthdate = $('#birthdate').val();
                        $("#dateOfBirthday").html(newBirthdate);
                        $("#formBirthdate").append('<div class="alert alert-success col-9 mt-2 text-center mx-auto" role="alert"><span>Data di nascita cambiata con successo!</span></div>');
                    } else if (response === "BIRTH_NOT_OK"){
                        $("#formBirthdate").append('<div class="alert alert-danger mt-2 col-9 text-center mx-auto" role="alert"><span>Inserire una data di nascita valida!</span></div>');
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                  console.error(errorThrown);
                }
              });
              });
            var datepicker = $('#birthdate').pickadate({
                    //  TODO: Scelta anno più vasta
                  formatSubmit: 'yyyy/mm/dd',
                  format: 'd/m/yyyy',
                  selectYears: 100,
                  selectMonths: true,
                  selectYears: true,
                  max: new Date(),
                  min: new Date(1900, 0, 1),
                  today: false,
                  clear: false,
                  close: false,
                  monthsFull: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'],
                  weekdaysShort: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab']
                });
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
                    '<h5 class="card-title text-center">Cambio Password</h5>' +
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

    function bodyGroups(type_data){
        let id = $('#id').val()
        let url_req = url + type_data + "/" + id;
        $.ajax({
            url: url_req,
            type: 'POST',
            success: function(data) {
                console.log(data);
                if (Array.isArray(data) && data.length === 0) {

                } else {

                    var admin = data["groups"];
                    var members = data["teamsUsers"];

                if(members.length === 0){
                    //  l'array è vuoto, nessun gruppo
                    $('#info-body').append("<p class=\"text-center\">Non sei presente in nessuno dei gruppi registrati</p>");
                } else {
                    var notAdminMembers = members.filter(function(member) {
                      return !admin.some(function(adminMember) {
                        return adminMember.id === member.id;
                      });
                    });

                $('#info-body').append(
                    '<table class=\"table text-center\">' +
                        '<thead>' +
                            '<tr><th scope=\"col\">Nome</th><th scope=\"col\">Visibilità</th><th scope=\"col\">Creato il</th><th scope=\"col\">Admin</th></tr>' +
                        '</thead><tbody id="groupsBodyTable"></tbody></table>');
                // supponiamo che 'data' sia l'array restituito dalla chiamata AJAX
                admin.forEach(function(element) {
                var dateTime = new Date(element["createdOn"]);
                var formattedDate = dateTime.toLocaleDateString('it-IT', { year: 'numeric', month: '2-digit', day: '2-digit' });
                var formattedTime = dateTime.toLocaleTimeString('it-IT');
                if(element["visibility"] == "PUBLIC"){
                    var lock = '<i class="fa-solid fa-unlock"></i>';
                } else {
                    var lock = '<i class="fa-solid fa-lock"></i>';
                }
                  $('#groupsBodyTable').append(
                    '<tr>' +
                          '<td>'+element["name"]+'</td>'+
                          '<td>'+lock+'</td>'+
                          '<td>'+(formattedDate + ' ' + formattedTime)+'</td>'+
                          '<td><i class="fa-solid fa-check"></i></td>'+
                        '</tr>'
                  );
                });
                notAdminMembers.forEach(function(element) {
                var dateTime = new Date(element["createdOn"]);
                var formattedDate = dateTime.toLocaleDateString('it-IT', { year: 'numeric', month: '2-digit', day: '2-digit' });
                var formattedTime = dateTime.toLocaleTimeString('it-IT');
                if(element["visibility"] == "PUBLIC"){
                    var lock = '<i class="fa-solid fa-unlock"></i>';
                } else {
                    var lock = '<i class="fa-solid fa-lock"></i>';
                }
                  $('#groupsBodyTable').append(
                    '<tr>' +
                          '<td>'+element["name"]+'</td>'+
                          '<td>'+lock+'</td>'+
                          '<td>'+(formattedDate + ' ' + formattedTime)+'</td>'+
                          '<td><i class="fa-solid fa-xmark"></i></td>'+
                        '</tr>'
                  );
                });
               }
            }
            },
             error: function(jqXHR, textStatus, errorThrown) {
               console.error(errorThrown);
             }
           });
    }

    function bodyImageUser(type_data){
        $('#info-body').append('<div class="card-body"><div class="row"><div class="col-md-3"><div class="profile-pic rounded-circle"><img src="path-to-image" alt="Profile Picture" class="img-fluid"><button class="btn btn-sm btn-outline-secondary mt-2">Cambia Foto</button></div></div><div class="col-md-9"><p>Testo della seconda colonna</p></div></div></div>');
    }
});