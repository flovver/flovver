<script lang="ts">
    import { makeDnD } from "../common/dnd-util";
    import { typesByName } from "./types/types";

    let viewportOffsetX: number = 0;
    let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        viewportOffsetX += e.movementX;
        viewportOffsetY += e.movementY;
    });

    export let types = [];

    let typeCounter = 1;

    function addType(name: string, e: DragEvent) {
        types.push({
            name: `t${typeCounter++}`,
            baseType: name,
            x: e.clientX - viewportOffsetX,
            y: e.clientY - viewportOffsetY,
            component: typesByName[name],
            deleteAction: (i) => {
                types.splice(i, 1);
                types = types;
                setCurrentType(null);
            },
        });

        types = types;
    }

    let isDragHovered = false;

    function onDrop(e: DragEvent) {
        isDragHovered = false;
        addType(e.dataTransfer.getData("typeName"), e);
    }

    export let currentType;

    function setCurrentType(t: any) {
        currentType = t;
    }
</script>

<svelte:window on:mouseup={onMouseUp} />

<div
    on:mousedown={(e) => {
        setCurrentType(null);
        onMouseDown(e);
    }}
    on:mousemove={onMouseMove}
    on:drop={onDrop}
    on:dragenter={(_) => (isDragHovered = true)}
    on:dragleave={(_) => (isDragHovered = false)}
    on:dragover={(e) => e.preventDefault()}
    class="fixed w-screen h-screen {isDragHovered
        ? 'bg-gray-200'
        : 'bg-gray-100'}"
/>

{#each types as t, i}
    <svelte:component
        this={t.component}
        bind:name={t.name}
        baseType={t.baseType}
        deleteAction={() => t.deleteAction(i)}
        {setCurrentType}
        bind:x={t.x}
        bind:y={t.y}
        bind:viewportOffsetX
        bind:viewportOffsetY
    />
{/each}
