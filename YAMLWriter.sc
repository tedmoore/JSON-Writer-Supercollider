/*
Ted Moore
ted@tedmooremusic.com
www.tedmooremusic.com
November 30, 2019

SuperCollider convenience class for creating a yaml file. It takes in a dictionary (or nested dictionaries) and converts them to a yaml file.

*/


YAMLWriter {

	*new {
		arg object, path;
		^super.new.init(object,path);
	}

	init {
		arg object, path;
		var file = File(path,"w");
		var yamlString = this.unpackObject(object);

		file.write(yamlString);

		file.close;

		^nil;
	}

	unpackObject {
		arg object, depth = 0;
		var returnString = "";

		object.keys.do({
			arg key, i;
			var item = object[key];

			(depth+1).do({
				arg i;
				returnString = returnString ++ "\t";
			});

			returnString = returnString ++ "%: %\n".format(key,this.item_to_return_string(item,depth));
		});

		returnString = "%\n".format(returnString);
		depth.do({
			arg i;
			returnString = returnString ++ "\t";
		});
		returnString = returnString;
		^returnString;
	}

	item_to_return_string {
		arg item, depth;
		var returnString;
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
			returnString = this.unpack_array(item,depth+1);
		}
		{item.isKindOf(Dictionary)}{
			returnString = this.unpackObject(item,depth+1);
		}
		{item.isKindOf(Boolean)}{
			returnString = item.asString;
		}
		{item.isKindOf(Char)}{
			returnString = item.asString.cs;
		}
		{item.isNil}{
			returnString = "null";
		}
		{	
			"% DON'T KNOW WHAT TO DO WITH THIS:".format(thisMethod).warn;
			item.postln;
			item.class.postln;
			"".postln;
			returnString = item.asCompileString;
		};

		^returnString;
	}

	unpack_array {
		arg array,depth;
		var returnString = "";
		array.do({
			arg item, idx;
			returnString = returnString ++ "%\n".format(this.item_to_return_string(item,depth));
		});
		// returnString = returnString ++ " ]";
		^returnString;
	}
}