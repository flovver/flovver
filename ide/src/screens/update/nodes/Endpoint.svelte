<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";

    export let title: string;
    export let titleAlignment: "left" | "right" = "left";

    export let x: number;
    export let y: number;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;
    });
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    on:mousedown={onMouseDown}
    class="fixed cursor-move flex items-center gap-2"
    style="left: {x}px; top: {y}px;"
>
    {#if titleAlignment == "left"}
        <div class="italic">{title}</div>
    {/if}
    <div
        class="bg-gray cursor-default border-gray-400 border-2 rounded-full w-3 h-3 {titleAlignment ==
        'left'
            ? 'mt-1'
            : ''}"
    />
    {#if titleAlignment == "right"}
        <div class="italic">{title}</div>
    {/if}
</div>
