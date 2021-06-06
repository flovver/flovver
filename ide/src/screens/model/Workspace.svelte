<script lang="ts">
    import { makeDnD } from "../common/dnd-util";
    import { typesByName } from "./types/types";

    let viewportOffsetX: number = 0;
    let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        viewportOffsetX += e.movementX;
        viewportOffsetY += e.movementY;
    });

    export let types: any[];

    let typeCounter = types.length + 1;

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
            model: false,
            setModel: (i) => {
                types.forEach((v, i, a) => (v.model = false));
                types[i].model = true;
            },
            message: false,
            setMessage: (i) => {
                types.forEach((v, i, a) => (v.message = false));
                types[i].message = true;
            },
            variants: name == 'Variant' ? [ { name: 'Var1', baseType: 'Unit' } ] : null,
        });

        types = types;
    }

    let isDragHovered = false;

    function onDrop(e: DragEvent) {
        isDragHovered = false;
        addType(e.dataTransfer.getData("typeName"), e);
    }

    export let currentType;

    export function setCurrentType(t: any) {
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
        bind:model={t.model}
        setModel={() => t.setModel(i)}
        bind:message={t.message}
        setMessage={() => t.setMessage(i)}
        bind:x={t.x}
        bind:y={t.y}
        bind:variants={t.variants}
        bind:viewportOffsetX
        bind:viewportOffsetY
    />
{/each}
