<script lang="ts">
    export let width: number = 800;
    export let height: number = 600;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    export let addWidget: (name: string, e: DragEvent) => void;

    let isDragHovered = false;

    function onDrop(e: DragEvent) {
        isDragHovered = false;
        addWidget(e.dataTransfer.getData("widgetName"), e);
    }

    export let setCurrentWidget: (any) => void;

    const setCurrentRefinedWidget = () =>
        setCurrentWidget({
            name: "Pane",
            width: width,
            setW: (v) => (width = v),
            height: height,
            setH: (v) => (height = v),
            edit: false,
            hasPosition: true,
        });
</script>

<div
    tabindex="0"
    on:mousedown={setCurrentRefinedWidget}
    on:dragenter={(_) => (isDragHovered = true)}
    on:dragleave={(_) => (isDragHovered = false)}
    on:drop={onDrop}
    on:dragover={(e) => e.preventDefault()}
    class="fixed {isDragHovered
        ? 'bg-gray-50'
        : 'bg-white'} shadow-lg focus:ring-2 focus:ring-blue-600"
    style="left: {viewportOffsetX}px; top: {viewportOffsetY}px; width: {width}px; height: {height}px"
/>
