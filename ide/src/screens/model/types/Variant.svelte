<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";

    export let name: string = "";
    export let baseType: string = "";

    // export let variants = [
        // { name: "NewInput", baseType: "String" },
        // { name: "ComputeFactorial", baseType: "Unit" },
    // ];
    export let variants = [];

    export let x: number;
    export let y: number;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;
    });
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    tabindex="0"
    on:mousedown={onMouseDown}
    class="fixed bg-white shadow rounded cursor-move focus:ring-2 focus:ring-blue-600"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY + y}px;"
>
    <div class="flex justify-around px-2 py-1">
        {name} <span class="mx-2 italic">of</span>
        <span class="font-medium">{baseType}</span>
    </div>
    {#each variants as v}
        <div
            class="flex flex-row justify-between border-t bg-gray-50 px-2 py-1"
        >
            {v.name}
            <div>
                <span class="mx-2 italic">of</span>
                <span class="font-medium">{v.baseType}</span>
            </div>
        </div>
    {/each}
</div>
