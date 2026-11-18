/* GH Viagens — comportamento mínimo da interface (sem dependências). */
(function () {
    'use strict';

    function ready(fn) {
        if (document.readyState !== 'loading') { fn(); } else { document.addEventListener('DOMContentLoaded', fn); }
    }

    ready(function () {
        // 1) Destaca no menu o item da página atual (vence o caminho mais específico).
        var path = location.pathname.replace(/\/+$/, '') || '/';
        var melhor = null, tamanho = -1;
        document.querySelectorAll('.gh-nav a[href]:not(.gh-nav-disabled)').forEach(function (a) {
            var p = a.pathname.replace(/\/+$/, '') || '/';
            if ((path === p || path.indexOf(p + '/') === 0) && p.length > tamanho) { melhor = a; tamanho = p.length; }
        });
        if (melhor) { melhor.classList.add('active'); melhor.setAttribute('aria-current', 'page'); }

        // 2) Menu lateral em telas pequenas.
        var menu = document.getElementById('ghSidebar');
        var fundo = document.getElementById('ghBackdrop');
        var botao = document.getElementById('ghMenuToggle');
        function abrir(aberto) {
            if (!menu) { return; }
            menu.classList.toggle('open', aberto);
            if (fundo) { fundo.classList.toggle('show', aberto); }
            if (botao) { botao.setAttribute('aria-expanded', aberto ? 'true' : 'false'); }
        }
        if (botao) { botao.addEventListener('click', function () { abrir(!menu.classList.contains('open')); }); }
        if (fundo) { fundo.addEventListener('click', function () { abrir(false); }); }
        document.addEventListener('keydown', function (e) { if (e.key === 'Escape') { abrir(false); } });
    });

    // 3) Confirmação antes de enviar formulários marcados com data-confirm (ex.: excluir).
    document.addEventListener('submit', function (e) {
        var msg = e.target.getAttribute && e.target.getAttribute('data-confirm');
        if (msg && !window.confirm(msg)) { e.preventDefault(); }
    });
})();
