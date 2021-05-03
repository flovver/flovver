<script lang="ts">
  import { onMount } from 'svelte';

  import ChevronDown from '../icons/ChevronDown.svelte';
  import Hammer from '../icons/Hammer.svelte';
  import Play from '../icons/Play.svelte';
  import Settings from '../icons/Settings.svelte';

  export let projectName: String;

  let menu = {
    show: false,
    element: null
  };

  onMount(() => {
    const handleOutsideClick = (event) => {
      if (menu.show && !menu.element.contains(event.target)) {
        menu.show = false;
      }
    };

    const handleEscape = (event) => {
      if (menu.show && event.key === 'Escape') {
        menu.show = false;
      }
    };

    document.addEventListener('click', handleOutsideClick, false);
    document.addEventListener('keyup', handleEscape, false);

    return () => {
      document.removeEventListener('click', handleOutsideClick, false);
      document.removeEventListener('keyup', handleEscape, false);
    };
  });
</script>

<nav class="bg-gray-800">
    <div class="mx-auto px-0">
      <div class="flex items-center justify-between h-12">
        <div class="flex items-center">
          <div class="md:block">
            <div class="flex items-baseline">
              <button class="bg-blue-600 text-white px-5 py-3.5 text-sm font-medium  focus:outline-none">Model</button>
              <button class="text-gray-200 hover:bg-gray-900 hover:text-white px-5 py-3.5 text-sm font-medium focus:outline-none">Update</button>
              <button class="text-gray-200 hover:bg-gray-900 hover:text-white px-5 py-3.5 text-sm font-medium focus:outline-none">View</button>

            </div>
          </div>
        </div>
        <div class="md:block">
            <div class="flex">
                <button title="Build" class="flex-shrink-0 px-3 hover:bg-gray-900 text-gray-300 hover:text-white focus:outline-none">
                    <Hammer/>
                </button>
                <button title="Run" class="flex-shrink-0 px-3 hover:bg-gray-900 text-gray-300 hover:text-white focus:outline-none">
                    <Play/>
                </button>
                <button title="Settings" class="flex-shrink-0 px-3 hover:bg-gray-900 text-gray-300 hover:text-white focus:outline-none">
                    <Settings/>
                </button>
                <div class="relative inline-block text-left" bind:this={menu.element}>
                    <button on:click={() => menu.show = !menu.show} type="button" class="inline-flex justify-center w-full px-4 py-3.5 text-sm font-medium text-white hover:bg-gray-900 focus:outline-none">
                      Project: {projectName}
                      <ChevronDown/>
                    </button>
                    {#if menu.show}
                      <div class="origin-top-right z-50 absolute right-2 mt-2 w-56 shadow-lg bg-white ring-1 ring-black ring-opacity-5 divide-y focus:outline-none">
                        <div class="py-1" role="none">
                          <button class="block px-4 py-2 w-full text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 focus:outline-none">
                            <div class="flex justify-between">
                              <div>Build</div>
                              <div class="text-gray-400">b</div>
                            </div>
                          </button>
                          <button class="block px-4 py-2 w-full text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 focus:outline-none">
                            <div class="flex justify-between">
                              <div>Run</div>
                              <div class="text-gray-400">r</div>
                            </div>
                          </button>
                          <button class="block px-4 py-2 w-full text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 focus:outline-none">
                            <div class="flex justify-between">
                              <div>Settings</div>
                              <div class="text-gray-400">p</div>
                            </div>
                          </button>
                        </div>
                        <div class="py-1" role="none">
                          <button class="block px-4 py-2 w-full text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900 focus:outline-none">
                            <div class="flex justify-between">
                              <div>Shut down IDE session</div>
                              <div class="text-gray-400">x-x</div>
                            </div>
                          </button>
                        </div>
                      </div>
                    {/if}
                </div>
            </div>
        </div>
      </div>
    </div>
</nav>