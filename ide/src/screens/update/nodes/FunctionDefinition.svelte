<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";
    import Moveable from "svelte-moveable";
    import Port from "./Port.svelte";

    import { addItem } from "./nodes";

    export let outerItems;
    export let refreshOuterItems;

    export let data;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    let target;
    let container;

    export let x: number;
    export let y: number;
    export let width: number = 200;
    export let height: number = 100;

    export let inputs = [];
    export let output = "Unit";

    let inputStartY: number;
    $: inputStartY = inputs.length > 1 ? height / 5 : height / 2;

    let inputGap: number;
    $: inputGap = inputs.length > 1 ? (height * 0.6) / (inputs.length - 1) : 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;

        items.forEach((item) => {
            item.x += e.movementX;
            item.y += e.movementY;
        });
        refreshOuterItems();

        target = null;
    });

    const clearTarget = (e: any) => {
        if (e.target.id == "update-bg") {
            target = null;
        }
    };

    export let items = [];

    let isDragHovered = false;

    function onDrop(e: DragEvent) {
        isDragHovered = false;
        addItem(
            "definition",
            data,
            items,
            e.dataTransfer.getData("defName"),
            e.clientX - viewportOffsetX,
            e.clientY - viewportOffsetY,
            outerItems
        );
        refreshOuterItems();
    }

    export let deleteAction;
    function deleteThis(e) {
        e.preventDefault();
        if (window.confirm("Delete function definition?")) {
            deleteAction();
        }
        return false;
    }
</script>

<svelte:window
    on:mousedown={clearTarget}
    on:mouseup={onMouseUp}
    on:mousemove={onMouseMove}
/>

<div
    bind:this={container}
    on:drop={onDrop}
    on:dragenter={(_) => (isDragHovered = true)}
    on:dragleave={(_) => (isDragHovered = false)}
    on:dragover={(e) => e.preventDefault()}
    on:mousedown={onMouseDown}
    on:mouseup={() => (target = container)}
    on:contextmenu={deleteThis}
    class="fixed cursor-move shadow border-2 border-gray-500 {isDragHovered
        ? 'bg-gray-600'
        : 'bg-gray-400'} bg-opacity-25 rounded"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY +
        y}px; width: {width}px; height: {height}px;"
>
    <div class="absolute text-sm italic" style="top: -24px;">
        ({inputs.length > 0 ? inputs.reduce((a, b, _) => a + ", " + b) : ""}) -> {output}
    </div>
</div>
{#each inputs as _, i}
    <Port
        x={viewportOffsetX + x - 6}
        y={viewportOffsetY + y + inputStartY + inputGap * i - 6}
        type="input"
        position="internal"
    />
{/each}
<Port
    x={viewportOffsetX + x + width - 6}
    y={viewportOffsetY + y + height / 2 - 6}
    type="output"
    position="internal"
/>
<svg
    class="fixed text-gray-500 h-3 top-full"
    style="left: {viewportOffsetX +
        x +
        width / 2 -
        3}px; top: {viewportOffsetY + y + height}px;"
    x="0px"
    y="0px"
    viewBox="0 0 255 255"
    xml:space="preserve"
    ><polygon class="fill-current" points="0,0 127.5,127.5 255,0" /></svg
>
<Port
    x={viewportOffsetX + x + width / 2 - 3}
    y={viewportOffsetY + y + height - 6}
    hideable={true}
    type="output"
    position="internal"
/>
<Moveable
    {target}
    resizable={true}
    on:resize={({ detail }) => {
        width = detail.clientX - x - viewportOffsetX;
        width = width > 0 ? width : 0;

        height = detail.clientY - y - viewportOffsetY;
        height = height > 0 ? height : 0;
    }}
/>
