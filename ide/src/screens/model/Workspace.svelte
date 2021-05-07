<script lang="ts">
    import { makeDnD } from "../common/dnd-util";
    import { typesByName } from "./types/types";

    let viewportOffsetX: number = 0;
    let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        viewportOffsetX += e.movementX;
        viewportOffsetY += e.movementY;
    });

    let types = [];

    function addType(name: string, e: DragEvent) {
        types.push({
            name: "t",
            baseType: name,
            x: e.clientX - viewportOffsetX,
            y: e.clientY - viewportOffsetY,
            component: typesByName[name],
        });

        types = types;
    }

    let isDragHovered = false;

    function onDrop(e: DragEvent) {
        isDragHovered = false;
        addType(e.dataTransfer.getData("typeName"), e);
    }
</script>

<svelte:window on:mouseup={onMouseUp} />

<div
    on:mousedown={onMouseDown}
    on:mousemove={onMouseMove}
    on:drop={onDrop}
    on:dragenter={(_) => (isDragHovered = true)}
    on:dragleave={(_) => (isDragHovered = false)}
    on:dragover={(e) => e.preventDefault()}
    class="fixed w-screen h-screen {isDragHovered
        ? 'bg-gray-200'
        : 'bg-gray-100'}"
/>

{#each types as t}
    <svelte:component
        this={t.component}
        bind:name={t.name}
        baseType={t.baseType}
        bind:x={t.x}
        bind:y={t.y}
        bind:viewportOffsetX
        bind:viewportOffsetY
    />
{/each}
