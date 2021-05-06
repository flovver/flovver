<script lang="ts">
    export let currentWidget;

    let x;
    let y;

    let width;
    let height;

    let caption;

    $: if (!currentWidget?.edit) x = currentWidget?.x;
    $: if (!currentWidget?.edit) y = currentWidget?.y;
    $: if (!currentWidget?.edit) width = currentWidget?.width;
    $: if (!currentWidget?.edit) height = currentWidget?.height;
    $: if (!currentWidget?.edit) caption = currentWidget?.caption;

    $: if (currentWidget?.edit && currentWidget.setCaption) {
        currentWidget.setCaption(caption);
    }
    $: if (currentWidget?.edit && currentWidget.setX) {
        currentWidget.setX(x);
    }
    $: if (currentWidget?.edit && currentWidget.setY) {
        currentWidget.setY(y);
    }
    $: if (currentWidget?.edit && currentWidget.setW) {
        currentWidget.setW(width);
    }
    $: if (currentWidget?.edit && currentWidget.setH) {
        currentWidget.setH(height);
    }

    function enableEdit() {
        if (currentWidget) {
            currentWidget.edit = true;
        }
    }
</script>

{#if currentWidget}
    <div>
        <div class="flex p-4 justyify-center">
            <div class="mr-2 text-gray-600">widget</div>
            <div>{currentWidget.name}</div>
        </div>
    </div>
    {#if currentWidget.caption}
        <div class="mt-4">
            <div class="px-4 font-medium text-gray-900">Specific</div>
            <div class="flex p-4 justyify-center">
                <div class="mr-2 text-gray-600">caption</div>
                <input
                    bind:value={caption}
                    on:input={enableEdit}
                    class="w-full"
                    type="text"
                />
            </div>
        </div>
    {/if}
    {#if currentWidget.hasPosition}
        <div class="mt-4">
            <div class="px-4 font-medium text-gray-900">Position</div>
            <div class="grid grid-cols-2 gap-4 p-4">
                {#if currentWidget.x}
                    <div class="flex justyify-center">
                        <div class="mr-2 text-gray-600">x</div>
                        <input
                            bind:value={x}
                            on:input={enableEdit}
                            class="w-full"
                            type="number"
                        />
                    </div>
                {/if}
                {#if currentWidget.y}
                    <div class="flex  justyify-center">
                        <div class="mr-2 text-gray-600">y</div>
                        <input
                            bind:value={y}
                            on:input={enableEdit}
                            class="w-full"
                            type="number"
                        />
                    </div>
                {/if}
                {#if currentWidget.setW}
                    <div class="flex  justyify-center">
                        <div class="mr-2 text-gray-600">width</div>
                        <input
                            bind:value={width}
                            on:input={enableEdit}
                            class="w-full"
                            type="number"
                        />
                    </div>
                {/if}
                {#if currentWidget.setH}
                    <div class="flex justyify-center">
                        <div class="mr-2 text-gray-600">height</div>
                        <input
                            bind:value={height}
                            on:input={enableEdit}
                            class="w-full"
                            type="number"
                        />
                    </div>
                {/if}
            </div>
        </div>
    {/if}
    {#if currentWidget.name != "Pane"}
        <div class="mt-4">
            <div class="px-4 font-medium text-gray-900">Extra</div>
            <div class="p-4">
                <button
                    on:click={currentWidget.deleteAction}
                    type="submit"
                    class="py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                >
                    Delete component
                </button>
            </div>
        </div>
    {/if}
{:else}
    <div class="p-4 text-center text-gray-600">
        Select widget in workspace to view its properties here
    </div>
{/if}
