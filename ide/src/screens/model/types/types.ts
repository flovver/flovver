import Alias from './Alias.svelte';
import Variant from './Variant.svelte';

export const typeList = [
    { title: 'Int' },
    { title: 'String' },
    { title: 'Boolean' },
    { title: 'Unit' },
    { title: 'Variant' },
];

export const typesByName = {
    'Int': Alias,
    'String': Alias,
    'Boolean': Alias,
    'Unit': Alias,
    'Variant': Variant,
};