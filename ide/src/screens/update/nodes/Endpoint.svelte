<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";
    import Port from "./Port.svelte";

    export let data;

    export let title: string;
    export let type: "input" | "output" = data.type == "model-output" ? "input" : "output";

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    export let x: number;
    export let y: number;

    let portBkg;

    let portOffsetX: number;
    let portOffsetY: number;

    $: {
        if (portOffsetX == undefined) {
            const rect = portBkg?.getBoundingClientRect();
            if (rect) {
                portOffsetX = rect.x - x;
                portOffsetY = rect.y - y;
            }
        }
    }

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;
    });
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    on:mousedown={onMouseDown}
    class="fixed cursor-move flex items-center gap-2"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY + y}px;"
>
    {#if type == "output"}
        <div class="italic">{title}</div>
    {/if}
    <div
        bind:this={portBkg}
        class="border-2 rounded-full w-3 h-3 {type ==
        'output'
            ? 'mt-1'
            : ''}"
    />
    {#if type == "input"}
        <div class="italic">{title}</div>
    {/if}
</div>
<Port x={viewportOffsetX + x + portOffsetX} y={viewportOffsetY + y + portOffsetY} type={type} />
