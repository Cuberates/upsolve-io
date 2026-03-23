document.addEventListener("DOMContentLoaded", function () {
    const solutionWrapper = document.getElementById("solution-wrapper");
    const toggleBtn = document.getElementById("btn-toggle-solution");
    const toggleText = toggleBtn ? toggleBtn.querySelector("span") : null;
    const toggleIcon = toggleBtn ? toggleBtn.querySelector("i") : null;
    const codeTextarea = document.getElementById("studyCode");
    const nextProblemGroup = document.getElementById("next-problem-group");

    let editor = null;

    if (window.CodeMirror && codeTextarea) {
        editor = CodeMirror.fromTextArea(codeTextarea, {
            lineNumbers: true,
            mode: "text/x-c++src",
            theme: "material-darker",
            indentUnit: 4,
            tabSize: 4,
            smartIndent: true,
            lineWrapping: false,
            matchBrackets: true,
            autoCloseBrackets: true,
            styleActiveLine: true
        });

        editor.setValue(
`#include <bits/stdc++.h>
using namespace std;

int main() {

    return 0;
}`
        );

        setTimeout(function () {
            editor.refresh();
        }, 100);

        window.addEventListener("resize", function () {
            editor.refresh();
        });
    }

    function setSolutionState(isVisible) {
        if (!solutionWrapper || !toggleBtn || !toggleText || !toggleIcon) return;

        solutionWrapper.classList.toggle("visible", isVisible);

        if (isVisible) {
            toggleText.textContent = "Hide Solution";
            toggleIcon.className = "bi bi-eye-slash";
            if (nextProblemGroup) nextProblemGroup.style.display = "flex";
        } else {
            toggleText.textContent = "Show Solution";
            toggleIcon.className = "bi bi-eye";
            if (nextProblemGroup) nextProblemGroup.style.display = "none";
        }

        if (editor) {
            setTimeout(function () {
                editor.refresh();
            }, 120);
        }
    }

    setSolutionState(false);

    if (toggleBtn) {
        toggleBtn.addEventListener("click", function () {
            const currentlyVisible = solutionWrapper.classList.contains("visible");
            setSolutionState(!currentlyVisible);
        });
    }
});