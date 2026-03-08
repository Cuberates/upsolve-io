
(function () {
    const wrapper = document.getElementById('account-logo');
    const dropdown = document.getElementById('account-dropdown');

    if (!wrapper || !dropdown) return;

    // Toggle dropdown open/close when clicking the account icon
    wrapper.addEventListener('click', function (e) {
        e.stopPropagation();
        dropdown.classList.toggle('show');
    });

    // Close dropdown when clicking anywhere else on the page
    document.addEventListener('click', function () {
        dropdown.classList.remove('show');
    });

    // Prevent clicks inside the dropdown from closing it
    dropdown.addEventListener('click', function (e) {
        e.stopPropagation();
    });

})();
