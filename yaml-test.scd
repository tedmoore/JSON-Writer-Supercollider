(
~unpackObject = {
	arg object, depth = 0;
	var returnString = "";

	object.keys.asArray.sort.do({
		arg key, i;
		var item = object[key];

		{returnString = returnString ++ "\t"} ! depth;

		if(item.isKindOf(Dictionary)){
			returnString = returnString ++ "%:\n%".format(key,~item_to_return_string.(item,depth+1));
		}{
			returnString = returnString ++ "%: %\n".format(key,~item_to_return_string.(item,depth+1));
		}
	});

	returnString;
};

~item_to_return_string = {
	arg item, depth = 0;
	var returnString = "";
	case
	{item.isString || item.isKindOf(Symbol)}{
		returnString = item.asString.cs;
	}
	{item.isNumber}{
		var return = nil;
		case
		{item == inf}{return = "inf".asCompileString}
		{item == -inf}{return = "-inf".asCompileString}
		{return = item.asCompileString};
		returnString = return;
	}
	{item.isSequenceableCollection.and(item.isString.not)}{
		returnString = ~unpack_array.(item,depth);
	}
	{item.isKindOf(Dictionary)}{
		returnString = ~unpackObject.(item,depth);
	}
	{item.isKindOf(Boolean)}{
		returnString = item.asString;
	}
	{item.isKindOf(Char)}{
		returnString = item.asString.cs;
	}
	{item.isKindOf(Color)}{
		returnString = item.asArray.cs;
	}
	{item.isKindOf(ControlSpec)}{
		var csd = Dictionary.new;
		csd['minval'] = item.minval;
		csd['maxval'] = item.maxval;
		csd['warp'] = item.warp;
		csd['step'] = item.step;
		csd['default'] = item.default;
		csd['units'] = item.units;
		csd['clipLo'] = item.clipLo;
		csd['clipHi'] = item.clipHi;
		returnString = ~item_to_return_string.(csd, depth + 1);
	}
	{item.isNil}{
		returnString = "null";
	}
	{
/*		"% DON'T KNOW WHAT TO DO WITH THIS:".format(thisMethod).warn;
		item.postln;
		item.class.postln;
		"".postln;*/
		returnString = item.asCompileString;
	};

	returnString;
};

~unpack_array = {
	arg array,depth;
	var returnString = "";
	array.do({
		arg item, idx;
		returnString = returnString ++ "\n";
		if(item.isKindOf(Dictionary).not){
			{returnString = returnString ++ "\t"} ! depth;
		};
		returnString = returnString ++ "- ";
		returnString = returnString ++ ~item_to_return_string.(item,depth+1);
	});
	returnString;
};

d = Dictionary.new;

d['dict-one'] = Dictionary.new;
d['neg-inf'] = -inf;
d['inf'] = inf;
d['dict-one']['a'] = "aee";
d['dict-one']['b'] = "bee";
d['dict-one']['c'] = "cee";

d['dict-two'] = Dictionary.new;
d['dict-two']['one'] = 1;
d['dict-two']['two'] = 2;

d['dict-three'] = Dictionary.new;
d['dict-three']['one-one'] = 1.1;
d['dict-three']['two-two'] = 2.2;

d['array1'] = [ 0, 1.5, 2, "three", \four ];

d['dict-one']['d'] = 101.1;

d['array2'] = [ 102, Dictionary.new ];

d['array2'][1]['test-test'] = "test-test";
d['array2'][1]['number'] = 1.2;
{ "\n".postln; } ! 20;
// ~item_to_return_string.(d).postln;
~item_to_return_string.(Object.readArchive("/Users/macprocomputer/Desktop/sccm/nina_guo/nina_guo_01.sccm")).postln;
nil;
)