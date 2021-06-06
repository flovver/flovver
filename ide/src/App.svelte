<script lang="ts">
	import Tailwindcss from "./tailwind/Tailwind.svelte";

	import NavBar from "./layout/NavBar.svelte";

	import Model from "./screens/Model.svelte";
	import Update from "./screens/Update.svelte";
	import View from "./screens/View.svelte";
	import Settings from "./screens/Settings.svelte";

	let currentScreen: any = "settings";

	let data = loadProject();
	$: data.then((v) => console.log(v));

	async function loadProject() {
		return fetch("/api/load").then((r) => r.json());
	}
</script>

<Tailwindcss />

{#await data then d}
	<header>
		<NavBar projectName={d.project.name} bind:currentScreen />
	</header>

	<main class="select-none">
		<Model model={d.model} bind:currentScreen />
		<Update bind:currentScreen />
		<View view={d.view} bind:currentScreen />
		<Settings flags={d.compiler.flags} bind:currentScreen />
	</main>
{/await}
