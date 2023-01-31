package net.minecraft.src;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;
	protected int toolMaterial;

	public ItemTool(int itemID, int damage, int toolMaterial, Block[] effectiveBlocks) {
		super(itemID);
		this.toolMaterial = toolMaterial;
		this.blocksEffectiveAgainst = effectiveBlocks;
		this.maxStackSize = 1;
		this.maxDamage = 32 << toolMaterial;
		if(toolMaterial == 3) {
			this.maxDamage *= 4;
		}

		this.efficiencyOnProperMaterial = (float)((toolMaterial + 1) * 2);
		this.damageVsEntity = damage + toolMaterial;
	}

	public float getStrVsBlock(ItemStack stack, Block block) {
		for(int i3 = 0; i3 < this.blocksEffectiveAgainst.length; ++i3) {
			if(this.blocksEffectiveAgainst[i3] == block) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public void onBlockDestroyed(ItemStack stack, int x, int y, int z, int i5) {
		stack.damageItem(1);
	}
}
