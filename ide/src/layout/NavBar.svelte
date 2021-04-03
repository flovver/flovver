<script lang="ts">
    import ChevronDown from '../icons/ChevronDown.svelte';
    import Hammer from '../icons/Hammer.svelte';
    import Play from '../icons/Play.svelte';
    import Settings from '../icons/Settings.svelte';
</script>

<nav class="bg-gray-800">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <div class="flex items-center">
          <div class="md:block">
            <div class="flex items-baseline space-x-4">
              <!-- Current: "bg-gray-900 text-white", Default: "text-gray-300 hover:bg-gray-700 hover:text-white" -->
              <a href="#" class="bg-gray-900 text-white px-3 py-2 rounded-md text-sm font-medium">Model</a>

              <a href="#" class="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Update</a>

              <a href="#" class="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium">View</a>

            </div>
          </div>
        </div>
        <div class="md:block">
            <div class="flex">
                <button class="ml-auto bg-gray-800 flex-shrink-0 p-1 rounded-full text-gray-400 hover:text-white focus:outline-none">
                    <span class="sr-only">Build</span>
                    <Hammer/>
                </button>
                <button class="ml-2 bg-gray-800 flex-shrink-0 p-1 rounded-full text-gray-400 hover:text-white focus:outline-none">
                    <span class="sr-only">Run</span>
                    <Play/>
                </button>
                <button class="ml-2 bg-gray-800 flex-shrink-0 p-1 rounded-full text-gray-400 hover:text-white focus:outline-none">
                    <span class="sr-only">Settings</span>
                    <Settings/>
                </button>
                <div class="relative inline-block text-left">
                    <div class="ml-3">
                        <button type="button" class="inline-flex justify-center w-full rounded-md px-4 py-2 text-sm font-medium text-white hover:bg-gray-700 focus:outline-none" id="options-menu" aria-expanded="true" aria-haspopup="true">
                        Project: Foo
                        <ChevronDown/>
                        </button>
                    </div>
                    <div class="hidden origin-top-right absolute right-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none" role="menu" aria-orientation="vertical" aria-labelledby="options-menu">
                        <div class="py-1" role="none">
                          <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" role="menuitem">Account settings</a>
                          <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" role="menuitem">Support</a>
                          <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" role="menuitem">License</a>
                          <form method="POST" action="#" role="none">
                            <button type="submit" class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" role="menuitem">
                              Sign out
                            </button>
                          </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      </div>
    </div>
</nav>