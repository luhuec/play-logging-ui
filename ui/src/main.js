import App from './App.svelte';

let app;

document.addEventListener("DOMContentLoaded", function (event) {
	const target = document.getElementById('app')
	const basepath = window.playloggingui_basepath || ""

	if (target) {
		app = new App({
			target: target,
			props: { basepath: basepath }
		});
	}
});

window.app = app;
export default app;