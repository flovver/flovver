<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";
    import Moveable from "svelte-moveable";

    export let target;
    let container;

    export let x: number;
    export let y: number;
    export let width: number = 200;
    export let height: number = 100;

    export let inputs = [];
    export let output = "Unit";

    let inputStartY: number;
    $: inputStartY = inputs.length > 1 ? height / 5 : height / 2;

    let inputGap: number;
    $: inputGap = inputs.length > 1 ? (height * 0.6) / (inputs.length - 1) : 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;
        target = null;
    });
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    bind:this={container}
    on:mousedown={onMouseDown}
    on:mouseup={() => (target = container)}
    class="fixed cursor-move shadow border-2 border-gray-500 bg-gray-400 bg-opacity-25 rounded"
    style="left: {x}px; top: {y}px; width: {width}px; height: {height}px;"
/>
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
    class="fixed text-gray-500 h-3 top-full"
    style="left: {x + width / 2 - 3}px; top: {y + height}px;"
    x="0px"
    y="0px"
    viewBox="0 0 255 255"
    xml:space="preserve"
    ><polygon class="fill-current" points="0,0 127.5,127.5 255,0" /></svg
>
<Moveable
    {target}
    resizable={true}
    on:resize={({ detail }) => {
        width = detail.clientX - x;
        width = width > 0 ? width : 0;

        height = detail.clientY - y;
        height = height > 0 ? height : 0;
    }}
/>
