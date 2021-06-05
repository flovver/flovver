<script lang="ts">
    export let x1: number;
    export let y1: number;

    export let x2: number;
    export let y2: number;

    let width: number = 0;
    let height: number = 0;

    let sx: number = 0;
    let sy: number = 0;

    $: {
        width = Math.abs(x2 - x1);
        width = width > 2 ? width : 2;
        height = Math.abs(y2 - y1);
        height = height > 2 ? height : 2;

        sx = Math.min(x1, x2);
        sy = Math.min(y1, y2);
    }
</script>

<svg
    class="fixed"
    style="left: {sx}px; top: {sy}px;"
    viewBox="0 0 {width} {height}"
    {width}
    {height}
    xmlns="http://www.w3.org/2000/svg"
>
    <line
        x1={x1 - sx}
        y1={y1 - sy}
        x2={x2 - sx}
        y2={y2 - sy}
        stroke="black"
        stroke-width="2"
    />
</svg>
