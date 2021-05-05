<script lang="ts">
    import { makeDnD } from "./dnd-util";

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

<input
    on:mousedown={onMouseDown}
    value={caption}
    type="text"
    readonly
    class="fixed border cursor-move focus:outline-none"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY +
        y}px; width: {width}px; height: {height}px;"
/>
