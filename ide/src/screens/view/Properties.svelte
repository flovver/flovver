<script lang="ts">
    export let currentWidget;

    let deleteComponent;

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
    $: if (!currentWidget?.edit) deleteComponent = currentWidget?.deleteAction;

    $: if (currentWidget?.edit) currentWidget?.setCaption(caption);
    $: if (currentWidget?.edit) currentWidget?.setX(x);
    $: if (currentWidget?.edit) currentWidget?.setY(y);
    $: if (currentWidget?.edit) currentWidget?.setW(width);
    $: if (currentWidget?.edit) currentWidget?.setH(height);

    function enableEdit() {
        if (currentWidget) {
            currentWidget.edit = true;
        }
    }
</script>

<div>
    <div class="flex p-4 justyify-center">
        <div class="mr-2 text-gray-600">widget</div>
        <div>{currentWidget ? currentWidget.name : ""}</div>
    </div>
</div>
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
<div class="mt-4">
    <div class="px-4 font-medium text-gray-900">Position</div>
    <div class="grid grid-cols-2 gap-4 p-4">
        <div class="flex justyify-center">
            <div class="mr-2 text-gray-600">x</div>
            <input
                bind:value={x}
                on:input={enableEdit}
                class="w-full"
                type="number"
            />
        </div>
        <div class="flex  justyify-center">
            <div class="mr-2 text-gray-600">y</div>
            <input
                bind:value={y}
                on:input={enableEdit}
                class="w-full"
                type="number"
            />
        </div>
        <div class="flex  justyify-center">
            <div class="mr-2 text-gray-600">width</div>
            <input
                bind:value={width}
                on:input={enableEdit}
                class="w-full"
                type="number"
            />
        </div>
        <div class="flex justyify-center">
            <div class="mr-2 text-gray-600">height</div>
            <input
                bind:value={height}
                on:input={enableEdit}
                class="w-full"
                type="number"
            />
        </div>
    </div>
</div>
<div class="mt-4">
    <div class="px-4 font-medium text-gray-900">Extra</div>
    <div class="p-4">
        <button
            on:click={deleteComponent}
            type="submit"
            class="py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
        >
            Delete component
        </button>
    </div>
</div>
