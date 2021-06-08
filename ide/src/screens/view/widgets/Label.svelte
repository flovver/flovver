<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";
    import LabelIcon from "../../../icons/widgets/Label.svelte";

    export let caption: string = "";

    export let x: number;
    export let y: number;
    export let width: number;
    export let height: number;

    export let oninp: string;
    export let onclk: string;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    export let deleteAction: () => void;
    export let setCurrentWidget: (any) => void;

    const setCurrentRefinedWidget = () =>
        setCurrentWidget({
            name: "Label",
            caption: caption,
            setCaption: (v) => (caption = v),
            x: x,
            setX: (v) => (x = v),
            y: y,
            setY: (v) => (y = v),
            width: width,
            setW: (v) => (width = v),
            height: height,
            setH: (v) => (height = v),
            oninp: oninp,
            setOninp: (v) => (oninp = v),
            onclk: onclk,
            setOnclk: (v) => (onclk = v),
            edit: false,
            hasPosition: true,
            deleteAction: deleteAction,
        });

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;
        setCurrentRefinedWidget();
    });

    function withCurrentWidget(handler: (e: MouseEvent) => void) {
        return (e: MouseEvent) => {
            setCurrentRefinedWidget();
            handler(e);
        };
    }
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    tabindex="0"
    on:mousedown={withCurrentWidget(onMouseDown)}
    class="fixed cursor-move px-2 py-1 focus:ring-2 focus:ring-blue-600"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY +
        y}px; width: {width}px; height: {height}px;"
>
    {#if caption.trim().length > 0}
        {caption}
    {:else}
        <LabelIcon />
    {/if}
</div>
