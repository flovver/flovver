import Endpoint from './Endpoint.svelte';
import FunctionDefinition from './FunctionDefinition.svelte';
import FunctionCall from './FunctionCall.svelte';

export const primitivesList = ['Definition', 'self'];

export const functions = {
    'If': { inputs: ["Bool", "() -> A", "() -> A"], output: "A" },
    'Eq': { inputs: ["A", "A"], output: "Bool" },
    'LEq': { inputs: ["A", "A"], output: "Bool" },
    "Mul": { inputs: ["Number", "Number"], output: "Number" },
    "Add": { inputs: ["Number", "Number"], output: "Number" },
    "Num1": { inputs: [], output: "Number" },
    "Num0": { inputs: [], output: "Number" },
    "StrToNum": { inputs: ["String"], output: "Number" },
    "Minus1": { inputs: ["Number"], output: "Number" },
    "Minus2": { inputs: ["Number"], output: "Number" },
    "Dispatch": { inputs: ["Message", "(NewInput) -> A", "(Compute) -> A"], output: "A" },
    "Identity": { inputs: ["A"], output: "A" }
};

export const componentsByType = {
    'model-input': Endpoint,
    'message-input': Endpoint,
    'definition': FunctionDefinition,
    'call': FunctionCall,
    'recursive-call': FunctionCall,
    'model-output': Endpoint,
};

export function addItem(destination: "screen" | "definition", data: any, items: any[], name: string, x: number, y: number, outerItems: any[] = null) {
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

        const item: any = {
            component: FunctionDefinition,
            type: 'definition',
            inputs: inputs,
            output: output,
            x: x,
            y: y,
            width: 200,
            height: 100,
        };
        if (outerItems) item.parent = data;

        outerItems?.push(item);
        items.push(item);
    } else if (name == "self") {
        const item: any = {
            component: FunctionCall,
            title: 'self',
            type: 'recursive-call',
            inputs: [...data.inputs],
            output: data.output,
            x: x,
            y: y,
        };
        if (outerItems) item.parent = data;

        outerItems?.push(item);
        items.push(item);
    } else {
        const fn = functions[name];

        const item: any = {
            component: FunctionCall,
            title: name,
            type: 'call',
            inputs: fn.inputs,
            output: fn.output,
            x: x,
            y: y,
        };
        if (outerItems) item.parent = data;

        outerItems?.push(item);
        items.push(item);
    }
}