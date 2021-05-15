<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";

    export let title: string;

    export let x: number;
    export let y: number;

    let element;

    let width: number;
    let height: number;

    $: {
        const rect = element?.getBoundingClientRect();
        if (rect) {
            width = rect.width;
            height = rect.height;
        }
    }

    export let inputs = [];
    export let output = "Unit";

    let inputStartY: number;
    $: inputStartY = inputs.length > 1 ? 14 : height / 2;

    const inputGap: number = 24;
    $: height = ((inputs.length || 1) - +(inputs.length > 1)) * inputGap + 28;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;
    });
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    bind:this={element}
    on:mousedown={onMouseDown}
    class="fixed cursor-move bg-white shadow rounded px-8 py-2"
    style="left: {x}px; top: {y}px; height: {height}px;"
>
    <div class="italic" style="margin-top: {height / 2 - 20}px;">{title}</div>
</div>
{#each inputs as _, i}
    <div
        class="fixed bg-gray-50 border-gray-400 border-2 rounded-full w-3 h-3"
        style="left: {x - 6}px; top: {y + inputStartY + inputGap * i - 6}px;"
    />
{/each}
<div
    class="fixed bg-gray-50 border-gray-400 border-2 rounded-full w-3 h-3"
    style="left: {x + width - 6}px; top: {y + height / 2 - 6}px;"
/>
<svg
    class="fixed text-gray-500 h-2 top-full"
    style="left: {x + width / 2}px; top: {y + height}px"
    x="0px"
    y="0px"
    viewBox="0 0 255 255"
    xml:space="preserve"
    ><polygon class="fill-current" points="0,0 127.5,127.5 255,0" /></svg
>
<div
    class="fixed hover:bg-gray-50 border-gray-400 hover:border-2 hover:rounded-full w-3 h-3"
    style="left: {x + width / 2}px; top: {y + height - 6}px"
/>
