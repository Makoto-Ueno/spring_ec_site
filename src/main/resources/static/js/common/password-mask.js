function togglePasswordVisibility(element) {
    const inputPassword = element.closest('.input-password');
    const passwordInput = inputPassword.getElementsByTagName('input')[0];
    const toggleIcon = inputPassword.getElementsByTagName('span')[0];

    if (toggleIcon.classList.contains("is-mask")) {
        passwordInput.type = "text";
        toggleIcon.classList.add("not-mask");
        toggleIcon.classList.remove("is-mask");
    } else {
        passwordInput.type = "password";
        toggleIcon.classList.add("is-mask");
        toggleIcon.classList.remove("not-mask");
    }
}
