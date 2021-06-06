<script lang="ts">
  import CheckBox from "./settings/CheckBox.svelte";

  export let currentScreen;

  export let flags;

  export let useTailCallElimination = flags["tail-call-elimination"];
  export let useFibonacciElimination = flags["fibonacci-elimination"];
  export let useCommonRecursionMemoization = flags["common-recursion-memoization"];

  let tailCall = useTailCallElimination;
  let fibonacci = useFibonacciElimination;
  let memoization = useCommonRecursionMemoization;

  function saveSettings() {
    useTailCallElimination = tailCall;
    useFibonacciElimination = fibonacci;
    useCommonRecursionMemoization = memoization;
  }
</script>

<div class={currentScreen == "settings" ? "visible" : "invisible"}>
  <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
    <div class="px-4 py-6 my-12 sm:px-0">
      <h1 class="text-2xl">Settings</h1>
      <div class="mt-10 sm:mt-0">
        <div class="mt-5">
          <div class="shadow overflow-hidden sm:rounded-md">
            <div class="px-4 py-5 bg-white space-y-6 sm:p-6">
              <fieldset>
                <legend class="text-base font-medium text-gray-900"
                  >Recursion optimization</legend
                >
                <div class="mt-4 space-y-4">
                  <CheckBox
                    id="tail"
                    let:title
                    disabled={true}
                    bind:checked={tailCall}
                    description="Optimize proper tail calls."
                  >
                    <div class:title>Tail call elimination</div>
                  </CheckBox>
                  <CheckBox
                    id="fibonacci"
                    let:title
                    disabled={true}
                    bind:checked={fibonacci}
                    description="Optimize Fibonacci recursion patterns."
                  >
                    <div class:title>Fibonacci recursion elimination</div>
                  </CheckBox>
                  <CheckBox
                    id="memoization"
                    let:title
                    bind:checked={memoization}
                    description="Cache results of recursive function calls."
                  >
                    <div class:title>
                      Common recursion memoization <b class="text-red-500"
                        >(experimental)</b
                      >
                    </div>
                  </CheckBox>
                </div>
              </fieldset>
            </div>
            <div class="px-4 py-3 bg-gray-50 text-right sm:px-6">
              <button
                on:click={saveSettings}
                type="submit"
                class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
              >
                Save changes
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
