<script lang="ts">
    import { makeDnD } from "../common/dnd-util";

    import Endpoint from "./nodes/Endpoint.svelte";
    import Connections from "./Connections.svelte";

    import { addItem } from "./nodes/nodes";

    let viewportOffsetX: number = 0;
    let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        viewportOffsetX += e.movementX;
        viewportOffsetY += e.movementY;
    });

    export let items = [];

    let isDragHovered = false;

    function onDrop(e: DragEvent) {
        isDragHovered = false;
        addItem(
            "screen",
            null,
            items,
            e.dataTransfer.getData("defName"),
            e.clientX - viewportOffsetX,
            e.clientY - viewportOffsetY
        );
        items = items;
    }

    function deleteItem(i: number) {
        items.splice(i, 1);
        items = items;
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
    id="update-bg"
    class="fixed w-screen h-screen {isDragHovered
        ? 'bg-gray-200'
        : 'bg-gray-100'}"
/>

<Connections />

<Endpoint
    x={500}
    y={200}
    title="Model"
    bind:viewportOffsetX
    bind:viewportOffsetY
/>
<Endpoint
    x={480}
    y={600}
    title="Message"
    bind:viewportOffsetX
    bind:viewportOffsetY
/>
<Endpoint
    x={1200}
    y={400}
    title="View"
    type="input"
    bind:viewportOffsetX
    bind:viewportOffsetY
/>

{#each items as item, i}
    <svelte:component
        this={item.component}
        deleteAction={() => deleteItem(i)}
        bind:data={item}
        bind:x={item.x}
        bind:y={item.y}
        bind:title={item.title}
        bind:inputs={item.inputs}
        bind:output={item.output}
        bind:width={item.width}
        bind:heigth={item.height}
        bind:viewportOffsetX
        bind:viewportOffsetY
    />
{/each}
