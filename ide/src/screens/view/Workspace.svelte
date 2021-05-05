<script lang="ts">
    import { makeDnD } from "./widgets/dnd-util";

    import TextBox from "./widgets/TextBox.svelte";

    let isDragHovered = false;

    let viewportOffsetX: number = 0;
    let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        viewportOffsetX += e.movementX;
        viewportOffsetY += e.movementY;
    });
</script>

<svelte:window on:mouseup={onMouseUp} />

<div
    on:mousedown={onMouseDown}
    on:mousemove={onMouseMove}
    on:dragenter={(_) => (isDragHovered = true)}
    on:dragleave={(_) => (isDragHovered = false)}
    on:drop={(e) => {
        isDragHovered = false;
        console.log(e);
    }}
    on:dragover={(e) => e.preventDefault()}
    class="fixed w-screen h-screen {isDragHovered
        ? 'bg-gray-200'
        : 'bg-gray-100'}"
/>
<TextBox x={500} y={100} bind:viewportOffsetX bind:viewportOffsetY />
