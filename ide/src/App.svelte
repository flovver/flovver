<script lang="ts">
	import Tailwindcss from './tailwind/Tailwind.svelte';

	import NavBar from './layout/NavBar.svelte';

	import Model from './screens/Model.svelte';
	import Update from './screens/Update.svelte';
	import View from './screens/View.svelte';
	import Settings from './screens/Settings.svelte';

	let currentScreen: any = 'view';
</script>

<Tailwindcss />

<header>
	<NavBar projectName="factorial" bind:currentScreen={currentScreen} />
</header>

<main class="select-none">
	{#if currentScreen == 'model'}
		<Model/>
	{:else if currentScreen == 'update'}
		<Update/>
	{:else if currentScreen == 'view'}
		<View/>
	{:else if currentScreen == 'settings'}
		<Settings/>
	{/if}
</main>
  