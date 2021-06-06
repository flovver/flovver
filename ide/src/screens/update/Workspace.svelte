<script lang="ts">
    import { makeDnD } from "../common/dnd-util";

    import Connections from "./Connections.svelte";

    import { addItem, componentsByType } from "./nodes/nodes";
    import { state } from "../common/global-state";

    export let update;

    let viewportOffsetX: number = 0;
    let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        viewportOffsetX += e.movementX;
        viewportOffsetY += e.movementY;
    });

    export let items: any[] = update.nodes
        .map((v, i, a) => {
            const r: any = {
                component: componentsByType[v.type],
                type: v.type,
                inputs: v.inputs,
                output: v.output,
                x: v.x,
                y: v.y,
            };
            if (v.name) r.title = v.name;
            if (v.width) r.width = v.width;
            if (v.height) r.height = v.height;
            if (v.parent) r.parent = v.parent;
            if (v.type == "definition") r.items = [];
            return r;
        })
        .map((v, i, a) => {
            if (v.parent) {
                const j = v.parent;
                v.parent = a[j];
                a[j].items.push(v);
            }
            return v;
        });

    $: {
        state.update((v) => {
            if (!v.update) {
                v.update = {};
            }
            if (!v.update.nodes) {
                v.update.nodes = {};
            }
            v.update.nodes = items;
            return v;
        });
    }

    function refreshItems() {
        items = items;
    }

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
        const item = items.splice(i, 1)[0];
        items =
            item.type == "definition"
                ? items.filter((v, i, a) => v.parent != item)
                : items;
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

{#each items as item, i}
    <svelte:component
        this={item.component}
        deleteAction={() => deleteItem(i)}
        refreshOuterItems={refreshItems}
        bind:items={item.items}
        bind:outerItems={items}
        bind:data={item}
        bind:x={item.x}
        bind:y={item.y}
        bind:title={item.title}
        bind:inputs={item.inputs}
        bind:output={item.output}
        bind:width={item.width}
        bind:height={item.height}
        bind:viewportOffsetX
        bind:viewportOffsetY
    />
{/each}
