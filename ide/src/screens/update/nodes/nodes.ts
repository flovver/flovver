import FunctionDefinition from './FunctionDefinition.svelte';
import FunctionCall from './FunctionCall.svelte';

export const primitivesList = ['Definition', 'self'];

export const functions = {
    'If': { inputs: ["Bool", "() -> Int", "() -> Int"], output: "Int" }
};

export function addItem(destination: "screen" | "definition", data: any, items: any[], name: string, x: number, y: number) {
    // guards
    if (destination == "screen") {
        if (name == "self") return;
    } else if (destination == "definition") {
        if (name == "Definition") return;
    }

    if (name == "Definition") {
        const types = window
            .prompt("Input function type signature", "() -> Unit")
            .split("->")
            .map(v => v.trim());

        const inputs = types[0].length == 2 ? [] : types[0].substr(1, types[0].length - 2).split(',');
        const output = types[1];

        items.push({
            component: FunctionDefinition,
            inputs: inputs,
            output: output,
            x: x,
            y: y,
            width: 200,
            height: 100,
        });
    } else if (name == "self") {
        items.push({
            component: FunctionCall,
            title: 'self',
            inputs: [...data.inputs],
            output: data.output,
            x: x,
            y: y,
        });
    } else {
        const fn = functions[name];

        items.push({
            component: FunctionCall,
            title: name,
            inputs: fn.inputs,
            output: fn.output,
            x: x,
            y: y,
        })
    }
}