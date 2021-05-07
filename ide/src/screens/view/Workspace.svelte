<script lang="ts">
    import { makeDnD } from "../common/dnd-util";
    import { widgetsByName } from "./widgets/widgets";
    import Pane from "./Pane.svelte";

    let viewportOffsetX: number = 0;
    let viewportOffsetY: number = 0;

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        viewportOffsetX += e.movementX;
        viewportOffsetY += e.movementY;
    });

    const screen = { width: window.innerWidth, height: window.innerHeight };
    const pane = { width: 800, height: 600 };

    $: viewportOffsetX = (screen.width - pane.width) / 2;
    $: viewportOffsetY = (screen.height - pane.height) / 2;

    window.addEventListener("resize", (_) => {
        screen.width = window.innerWidth;
        screen.height = window.innerHeight;
    });

    let widgets = [];

    function addWidget(name: string, e: DragEvent) {
        const widgetPosition = widgets.length;

        widgets.push({
            caption: name,
            x: e.clientX - viewportOffsetX,
            y: e.clientY - viewportOffsetY,
            component: widgetsByName[name],
            deleteAction: () => {
                widgets.splice(widgetPosition, 1);
                widgets = widgets;
                setCurrentWidget(null);
            },
        });

        widgets = widgets;
    }

    export let currentWidget;

    function setCurrentWidget(w: any) {
        currentWidget = w;
    }
</script>

<svelte:window on:mouseup={onMouseUp} />

<div
    on:mousedown={(e) => {
        setCurrentWidget(null);
        onMouseDown(e);
    }}
    on:mousemove={onMouseMove}
    class="fixed w-screen h-screen bg-gray-100"
/>
<Pane
    {addWidget}
    {setCurrentWidget}
    bind:width={pane.width}
    bind:height={pane.height}
    bind:viewportOffsetX
    bind:viewportOffsetY
/>

{#each widgets as widget}
    <svelte:component
        this={widget.component}
        bind:caption={widget.caption}
        deleteAction={widget.deleteAction}
        {setCurrentWidget}
        bind:x={widget.x}
        bind:y={widget.y}
        bind:viewportOffsetX
        bind:viewportOffsetY
    />
{/each}
