/*
Ted Moore
ted@tedmooremusic.com
www.tedmooremusic.com
November 30, 2019

SuperCollider convenience class for creating a json file. It takes in a dictionary (or nested dictionaries) and converts them to a json file.

*/


JSONWriter {

	*new {
		arg object, path;
		^super.new.init(object,path);
	}

	init {
		arg object, path;
		var file = File(path,"w");
		var jsonString = this.unpackObject(object);

		file.write(jsonString);

		file.close;

		^nil;
	}

	unpackObject {
		arg object, depth = 0;
		var returnString = "{\n";

		object.keys.do({
			arg key, i;
			var item = object[key];
			//"key: %\t\titem: %".format(key,item).postln;
			//"item class: %\n".format(item.class).postln;

			/*returnString = returnString ++ "\n";*/

			(depth+1).do({
				arg i;
				returnString = returnString ++ "\t";
			});

			returnString = returnString ++ "\"%\":%".format(key,this.item_to_return_string(item,depth));

			if(i != (object.keys.size-1),{
				returnString = returnString ++ ",\n";
			})
		});

		returnString = "%\n".format(returnString);
		depth.do({
			arg i;
			returnString = returnString ++ "\t";
		});
		returnString = returnString ++ "}";
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
		{item.isKindOf(Event).or(item.isKindOf(Dictionary)).or(item.isKindOf(IdentityDictionary))}{
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
			"ERROR: DON'T KNOW WHAT TO DO WITH THIS:".warn;
			item.postln;
			item.class.postln;
			"".postln;
			returnString = item.asCompileString;
		};

		^returnString;
	}

	unpack_array {
		arg array,depth;
		var returnString = "[ ";
		array.do({
			arg item, idx;
			returnString = returnString ++ this.item_to_return_string(item,depth);

			if(idx < (array.size-1),{
				returnString = returnString ++ ", ";
			});

		});
		returnString = returnString ++ " ]";
		//returnString.postln;
		^returnString;
	}
}