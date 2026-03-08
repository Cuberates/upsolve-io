
(function () {

    const difficultyInput = document.getElementById('problemDifficulty');
    const difficultyBtns = document.querySelectorAll('.difficulty-btn');

    difficultyBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            const value = btn.dataset.value;

            difficultyInput.value = value;

            difficultyBtns.forEach(function (b) {
                b.classList.remove('active-easy', 'active-medium', 'active-hard');
            });

            if (value === '1') btn.classList.add('active-easy');
            else if (value === '2') btn.classList.add('active-medium');
            else if (value === '3') btn.classList.add('active-hard');
        });
    });

})();
