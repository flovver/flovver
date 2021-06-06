<script lang="ts">
	import { onDestroy } from "svelte";

	import Tailwindcss from "./tailwind/Tailwind.svelte";

	import NavBar from "./layout/NavBar.svelte";

	import Model from "./screens/Model.svelte";
	import Update from "./screens/Update.svelte";
	import View from "./screens/View.svelte";
	import Settings from "./screens/Settings.svelte";

	import { state, renderProjectJson } from "./screens/common/global-state";

	let currentScreen: any = "settings";

	let data = loadProject();

	async function loadProject() {
		return fetch("/api/load").then((r) => r.json());
	}

	async function saveProject() {
		const body = renderProjectJson(programState);

		return fetch("/api/save", {
			method: "POST",
			headers: {
				Accept: "application/json",
				"Content-Type": "application/json",
			},
			body: body,
		});
	}

	let programState;

	const unsubscribe = state.subscribe((value) => {
		programState = value;
	});

	onDestroy(unsubscribe);
</script>

<Tailwindcss />

{#await data then d}
	<header>
		<NavBar {saveProject} projectName={d.project.name} bind:currentScreen />
	</header>

	<main class="select-none">
		<Model model={d.model} bind:currentScreen />
		<Update bind:currentScreen />
		<View view={d.view} bind:currentScreen />
		<Settings
			projectName={d.project.name}
			flags={d.compiler.flags}
			bind:currentScreen
		/>
	</main>
{/await}
