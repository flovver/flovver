<script lang="ts">
    import { makeDnD } from "./dnd-util";

    export let caption: string = "";

    export let x: number;
    export let y: number;
    export let width: number;
    export let height: number;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    export let setCurrentWidget: (any) => void;

    const setCurrentRefinedWidget = () =>
        setCurrentWidget({
            name: "Button",
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
            edit: false,
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

<input
    on:mousedown={withCurrentWidget(onMouseDown)}
    value={caption}
    type="button"
    readonly
    class="fixed border cursor-move px-2 py-1 bg-gray-50 hover:bg-gray-50 focus:ring-2 focus:ring-blue-600"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY +
        y}px; width: {width}px; height: {height}px;"
/>
