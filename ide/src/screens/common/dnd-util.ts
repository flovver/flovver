export function makeDnD(action: (e: MouseEvent) => void) {
    let moving = false;

    return {
        onMouseDown: (_: MouseEvent) => moving = true,
        onMouseUp: (_: MouseEvent) => moving = false,
        onMouseMove: (e: MouseEvent) => { if (moving) action(e); }
    }
}