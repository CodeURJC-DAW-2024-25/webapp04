//VALIDATE FORM
const formulario = document.getElementById("form");
console.log('Formulario: ',formulario);
formulario.addEventListener('submit', (error)=>{
    validarFormulario();
    if (validarFormulario() && true){
        return true;
    }else{
        error.preventDefault();
    }
});

function validarFormulario(){
    const username = document.getElementById('name').value;
    const password = document.getElementById('password').value;
    const password2 = document.getElementById('confirmPassword').value;
    const email = document.getElementById('mail').value;
    return validateName(username) & validateMail(email) & validatePassword(password, password2);
}
//Validate username
function validateName(name) {
    const error = document.getElementById('errorName');
    if (name === '' || name == null) {  //Check if the name is empty
        error.innerText = 'Introduzca su nombre completo';
        error.classList.replace('ocult', 'error');
        return false;
    } else if (name.length > 16) {      //Check if the name is longer than 16 characters
        error.innerText = 'El nombre no debe exceder los 16 caracteres';
        error.classList.replace('ocult', 'error');
        return false;
    } else {
        error.innerText = '';
        error.classList.replace('error', 'ocult');
        return true;
    }
}

//Validate mail
function validateMail(mail) {
    const error = document.getElementById('errorMail');
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;    //Email regular expression
    if (mail === '' || mail == null) {                  //Check if the email is empty
        error.innerText = 'Introduzca su correo electrónico';
        error.classList.replace('ocult', 'error');
        return false;
    } else if (!emailRegex.test(mail)) {                //Check if the email is valid
        error.innerText = 'El correo electrónico no es válido';
        error.classList.replace('ocult', 'error');
        return false;
    } else {          
        error.innerText = '';
        error.classList.replace('error', 'ocult');
        return true;
    }
}

//Validate passwords
function validatePassword(password, confirmPassword) {
    const errorPassword = document.getElementById('errorPassword');
    if ((password === '' || password == null)||(confirmPassword === '' || confirmPassword == null)){      //Check if the password is empty
        errorPassword.innerText = 'Introduzca una contraseña';
        errorPassword.classList.replace('ocult', 'error');
        return false;
    } else if (password !== confirmPassword) {  //Check if the passwords match
        errorPassword.innerText = 'Las contraseñas no coinciden';
        errorPassword.classList.replace('ocult', 'error');
        return false;
    } else {
        errorPassword.innerText = '';
        errorPassword.classList.replace('error', 'ocult');
        return true;
    }
}