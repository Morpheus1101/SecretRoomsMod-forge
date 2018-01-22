package com.wynprice.secretroomsmod.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.FMLLog;

public class UVTransformer implements IClassTransformer
{
	
	public UVTransformer() {
        FMLLog.info("[UVTransformer] Initialized.");
    }

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.block.model.FaceBakery"))
            return basicClass;
		String methodStoreVertexName = SRMCore.isDebofEnabled ? "func_178404_a" : "storeVertexData";
        String methodStoreVertexDesc = "([IIILorg/lwjgl/util/vector/Vector3f;ILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;)V";
		
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode storeVertexData = new MethodNode(Opcodes.ACC_PUBLIC, methodStoreVertexName, methodStoreVertexDesc, null, new String[0]);
	    LabelNode startLabel = new LabelNode();
        LabelNode endLabel = new LabelNode();
        storeVertexData.instructions = new InsnList();
        storeVertexData.instructions.add(startLabel);
        storeVertexData.instructions.add(new LineNumberNode(152, startLabel)); //Required
        
        LocalVariableNode faceData = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_1_" : "faceData", "[I", null, startLabel, endLabel, 0);
        LocalVariableNode storeIndex = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_2_" :"storeIndex", "I", null, startLabel, endLabel, 1);
        LocalVariableNode vertexIndex = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_3_" : "vertexIndex", "I", null, startLabel, endLabel, 2);
        LocalVariableNode position = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_4_" : "position", "Lorg/lwjgl/util/vector/Vector3f;", null, startLabel, endLabel, 3);
        LocalVariableNode shadeColor = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_5_" : "shadeColor", "I", null, startLabel, endLabel, 4);
        LocalVariableNode sprite = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_6_" : "sprite", "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", null, startLabel, endLabel, 5);
        LocalVariableNode faceUV = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_7_" : "faceUV", "Lnet/minecraft/client/renderer/block/model/BlockFaceUV;", null, startLabel, endLabel, 6);
        storeVertexData.localVariables = Lists.newArrayList(faceData, storeIndex, vertexIndex, position, shadeColor, sprite, faceUV);
        
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ILOAD, 5));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 6));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 7));
        
        storeVertexData.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/UVTransformer", "storeVertexData", "([IIILorg/lwjgl/util/vector/Vector3f;ILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;)V", false));
        storeVertexData.instructions.add(new InsnNode(Opcodes.RETURN));
        storeVertexData.instructions.add(endLabel);
        
        ArrayList<MethodNode> nodes = new ArrayList<>();
        for(MethodNode m : node.methods)
        	if(!m.desc.equalsIgnoreCase(methodStoreVertexDesc) || !m.name.equalsIgnoreCase(methodStoreVertexName))
        		nodes.add(m);
        
        nodes.add(storeVertexData);
        
        node.methods.clear();
        node.methods.addAll(nodes);
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        basicClass = writer.toByteArray();
		return basicClass;
	}
	
	private static final HashMap<Vec3i, HashMap<Vec3i, BlockFaceUV>> BLOCKUV_DATA = new HashMap<>();
	
	public static void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV)
    {
        int i = storeIndex * 7;
        int i1 = Float.floatToRawIntBits(position.x);
        int i2 = Float.floatToRawIntBits(position.y);
        int i3 = Float.floatToRawIntBits(position.z);
        int i4 = shadeColor;
        int i5 = Float.floatToRawIntBits(sprite.getInterpolatedU((double)faceUV.getVertexU(vertexIndex) * .999 + faceUV.getVertexU((vertexIndex + 2) % 4) * .001));
        int i6 = Float.floatToRawIntBits(sprite.getInterpolatedV((double)faceUV.getVertexV(vertexIndex) * .999 + faceUV.getVertexV((vertexIndex + 2) % 4) * .001));
        int[] allInts = {i1, i2, i3, i4, i5, i6};
        for(int j = 0; j < 6; j++)
        	faceData[i + j] = allInts[j];
        if(!BLOCKUV_DATA.containsKey(new Vec3i(i1, i2, i3)))
        	BLOCKUV_DATA.put(new Vec3i(i1, i2, i3), new HashMap<>());
        BLOCKUV_DATA.get(new Vec3i(i1, i2, i3)).put(new Vec3i(i4, i5, i6), faceUV);
    }
	
	public static BlockFaceUV getUV(int[] list, int pos)
	{
		Vec3i vec1 = new Vec3i(list[pos], list[pos + 1], list[pos + 2]);
		Vec3i vec2 = new Vec3i(list[pos + 3], list[pos + 4], list[pos + 5]);
		return BLOCKUV_DATA.containsKey(vec1) ? BLOCKUV_DATA.get(vec1).get(vec2) : null;
	}

}