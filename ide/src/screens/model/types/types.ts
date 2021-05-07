import Alias from './Alias.svelte';
import Variant from './Variant.svelte';

export const typeList = [
    { title: 'Int' },
    { title: 'String' },
    { title: 'Boolean' },
    { title: 'Unit' },
    { title: 'Variant' },
];

export const builtinVariants = ['Int', 'String', 'Boolean', 'Unit'];

export const typesByName = {
    'Int': Alias,
    'String': Alias,
    'Boolean': Alias,
    'Unit': Alias,
    'Variant': Variant,
};