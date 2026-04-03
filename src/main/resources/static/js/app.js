function togglePassword(inputId, button) {
      const input = document.getElementById(inputId);
      if (!input) return;
      input.type = input.type === 'password' ? 'text' : 'password';
      button.setAttribute('aria-pressed', input.type === 'text');
    }

function validateRegisterForm() {
  const password = document.getElementById('userPassword');
  const confirmPassword = document.getElementById('confirmPassword');
  const error = document.getElementById('passwordMatchError');

  if (!password || !confirmPassword || !error) return true;

  if (password.value !== confirmPassword.value) {
    error.style.display = 'block';
    confirmPassword.focus();
    return false;
  }

  error.style.display = 'none';
  return true;
}

document.addEventListener('DOMContentLoaded', function () {
  const password = document.getElementById('userPassword');
  const confirmPassword = document.getElementById('confirmPassword');
  const error = document.getElementById('passwordMatchError');

  if (password && confirmPassword && error) {
    function checkPasswords() {
      if (confirmPassword.value === '') {
        error.style.display = 'none';
      } else if (password.value !== confirmPassword.value) {
        error.style.display = 'block';
      } else {
        error.style.display = 'none';
      }
    }

    password.addEventListener('input', checkPasswords);
    confirmPassword.addEventListener('input', checkPasswords);
  }
});