function togglePassword(inputId, button) {
      const input = document.getElementById(inputId);
      if (!input) return;
      input.type = input.type === 'password' ? 'text' : 'password';
      button.setAttribute('aria-pressed', input.type === 'text');
  }

  document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('registerForm');
  const email = document.getElementById('userEmail');
  const confirmEmail = document.getElementById('confirmedUserEmail');
  const password = document.getElementById('userPassword');
  const confirmPassword = document.getElementById('confirmedUserPassword');

  const emailError = document.getElementById('emailMatchError');
  const passwordError = document.getElementById('passwordMatchError');

  function validateEmailMatch() {
    const emailValue = email.value.trim();
    const confirmEmailValue = confirmEmail.value.trim();

    if (confirmEmailValue === '') {
      emailError.textContent = '';
      confirmEmail.classList.remove('invalid');
      return true;
    }

    if (emailValue !== confirmEmailValue) {
      emailError.textContent = 'Emails do not match.';
      confirmEmail.classList.add('invalid');
      return false;
    }

    emailError.textContent = '';
    confirmEmail.classList.remove('invalid');
    return true;
  }

  function validatePasswordMatch() {
    const passwordValue = password.value;
    const confirmPasswordValue = confirmPassword.value;

    if (confirmPasswordValue === '') {
      passwordError.textContent = '';
      confirmPassword.classList.remove('invalid');
      return true;
    }

    if (passwordValue !== confirmPasswordValue) {
      passwordError.textContent = 'Passwords do not match.';
      confirmPassword.classList.add('invalid');
      return false;
    }

    passwordError.textContent = '';
    confirmPassword.classList.remove('invalid');
    return true;
  }

  confirmEmail.addEventListener('input', validateEmailMatch);
  email.addEventListener('input', validateEmailMatch);

  confirmPassword.addEventListener('input', validatePasswordMatch);
  password.addEventListener('input', validatePasswordMatch);

  form.addEventListener('submit', function (event) {
    const isEmailValid = validateEmailMatch();
    const isPasswordValid = validatePasswordMatch();

    if (!isEmailValid || !isPasswordValid) {
      event.preventDefault();
    }
  });
});