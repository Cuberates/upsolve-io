
(function () {
    const wrapper = document.getElementById('account-logo');
    const dropdown = document.getElementById('account-dropdown');

    if (!wrapper || !dropdown) return;

    wrapper.addEventListener('click', function (e) {
        e.stopPropagation();
        dropdown.classList.toggle('show');
    });

    document.addEventListener('click', function () {
        dropdown.classList.remove('show');
    });

    dropdown.addEventListener('click', function (e) {
        e.stopPropagation();
    });

})();
