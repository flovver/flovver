<script lang="ts">
	import Tailwindcss from './tailwind/Tailwind.svelte';
	import NavBar from './layout/NavBar.svelte';
	import SideBar from './layout/SideBar.svelte';
</script>

<Tailwindcss />

<header>
	<NavBar projectName="factorial" />
</header>

<main>
	<SideBar title="Widgets" position="left">
		
	</SideBar>
</main>
  