<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";
    import Port from "./Port.svelte";

    export let data;

    export let title: string;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

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

    export let deleteAction;
    function deleteThis(e) {
        e.preventDefault();
        if (window.confirm(`Delete function "${title}"?`)) {
            deleteAction();
        }
        return false;
    }
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    class="fixed text-sm italic"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY + y - 24}px;"
>
    ({inputs.length > 0 ? inputs.reduce((a, b, _) => a + ", " + b) : ""}) -> {output}
</div>
<div
    bind:this={element}
    on:mousedown={onMouseDown}
    on:contextmenu={deleteThis}
    class="fixed cursor-move bg-white shadow rounded px-8 py-2"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY +
        y}px; height: {height}px;"
>
    <div class="italic" style="margin-top: {height / 2 - 20}px;">{title}</div>
</div>
{#each inputs as _, i}
    <Port
        node={data}
        index={i}
        x={viewportOffsetX + x - 6}
        y={viewportOffsetY + y + inputStartY + inputGap * i - 6}
        type="input"
    />
{/each}
<Port
    node={data}
    x={viewportOffsetX + x + width - 6}
    y={viewportOffsetY + y + height / 2 - 6}
    type="output"
/>
<svg
    class="fixed text-gray-500 h-2 top-full"
    style="left: {viewportOffsetX + x + width / 2}px; top: {viewportOffsetY +
        y +
        height}px"
    x="0px"
    y="0px"
    viewBox="0 0 255 255"
    xml:space="preserve"
    ><polygon class="fill-current" points="0,0 127.5,127.5 255,0" /></svg
>
<Port
    node={data}
    passBy={"thunk"}
    x={viewportOffsetX + x + width / 2}
    y={viewportOffsetY + y + height - 6}
    hideable={true}
    type="output"
/>
