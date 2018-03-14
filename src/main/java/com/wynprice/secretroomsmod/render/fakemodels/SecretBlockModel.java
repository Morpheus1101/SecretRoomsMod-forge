package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * The Model used to replace blocks with 
 * @author Wyn Price
 *
 */
public class SecretBlockModel implements IBakedModel
{
	
	/**
	 * The ThreadLocal used to control what {@link #isAmbientOcclusion()} should return
	 */
	public final ThreadLocal<Boolean> AO = ThreadLocal.withInitial(() -> false);
	
	public final ThreadLocal<IBlockState> RENDERSTATE = ThreadLocal.withInitial(() -> Blocks.STONE.getDefaultState());
	public final ThreadLocal<IBlockState> SRMSTATE = ThreadLocal.withInitial(() -> null);

	
	private final IBakedModel model;

	public SecretBlockModel(IBakedModel model) {
		this.model = model;
	}
	
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		IBlockState renderState = RENDERSTATE.get();
		if(SRMSTATE.get() != null)
		{
			state = SRMSTATE.get();
			FakeBlockModel renderModel = ((ISecretBlock)state.getBlock()).phaseModel(new FakeBlockModel(renderState));
			if(TrueSightHelmet.isHelmet()) {
    			renderModel = ((ISecretBlock)state.getBlock()).phaseTrueModel(new TrueSightModel(new FakeBlockModel(renderState)));
    		}
			return renderModel.setCurrentRender(state).getQuads(renderState, side, rand);
		}
		return this.model.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return AO.get();
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() 
	{
		return model.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return model.getOverrides();
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return model.handlePerspective(cameraTransformType);
	}
	
}
