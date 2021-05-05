<script lang="ts">
    import { makeDnD } from "./dnd-util";

    import LabelIcon from "../../../icons/widgets/Label.svelte";

    export let caption: string = "";

    export let x: number;
    export let y: number;
    export let width: number;
    export let height: number;

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
    class="fixed cursor-move px-2 py-1 focus:ring-2 focus:ring-blue-600"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY +
        y}px; width: {width}px; height: {height}px;"
>
    {#if caption.trim().length > 0}
        {caption}
    {:else}
        <LabelIcon />
    {/if}
</div>
