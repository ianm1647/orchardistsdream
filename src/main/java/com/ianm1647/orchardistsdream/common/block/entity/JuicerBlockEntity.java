package com.ianm1647.orchardistsdream.common.block.entity;

import com.google.common.collect.Lists;
import com.ianm1647.orchardistsdream.common.block.JuicerBlock;
import com.ianm1647.orchardistsdream.common.block.entity.container.JuicerMenu;
import com.ianm1647.orchardistsdream.common.block.entity.inventory.JuicerItemHandler;
import com.ianm1647.orchardistsdream.common.crafting.JuicerRecipe;
import com.ianm1647.orchardistsdream.common.registry.ODBlockEntityTypes;
import com.ianm1647.orchardistsdream.common.registry.ODItems;
import com.ianm1647.orchardistsdream.common.registry.ODRecipeTypes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JuicerBlockEntity extends SyncedBlockEntity implements MenuProvider, Nameable, RecipeHolder {
   public static final int DRINK_DISPLAY_SLOT = 2;
   public static final int CONTAINER_SLOT = 3;
   public static final int OUTPUT_SLOT = 4;
   public static final int INVENTORY_SIZE = 5;
   private final ItemStackHandler inventory = this.createHandler();
   private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new JuicerItemHandler(this.inventory, Direction.UP));
   private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> new JuicerItemHandler(this.inventory, Direction.DOWN));
   public int juiceTime;
   private int juiceTimeTotal;
   private ItemStack drinkContainerStack;
   private Component customName;
   protected final ContainerData kegData;
   private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;
   private ResourceLocation lastRecipeID;
   private boolean checkNewRecipe;

   public JuicerBlockEntity(BlockPos pos, BlockState state) {
      super(ODBlockEntityTypes.JUICER.get(), pos, state);
      this.drinkContainerStack = ItemStack.EMPTY;
      this.kegData = this.createIntArray();
      this.usedRecipeTracker = new Object2IntOpenHashMap();
      this.checkNewRecipe = true;
   }

   public static ItemStack getDrinkFromItem(ItemStack itemStack) {
      if (!itemStack.is((Item) ODItems.JUICER.get())) {
         return ItemStack.EMPTY;
      } else {
         CompoundTag compound = itemStack.getTagElement("BlockEntityTag");
         if (compound != null) {
            CompoundTag inventoryTag = compound.getCompound("Inventory");
            if (inventoryTag.contains("Items", 9)) {
               ItemStackHandler handler = new ItemStackHandler();
               handler.deserializeNBT(inventoryTag);
               return handler.getStackInSlot(2);
            }
         }

         return ItemStack.EMPTY;
      }
   }

   public static void takeServingFromItem(ItemStack itemStack) {
      if (itemStack.is((Item)ODItems.JUICER.get())) {
         CompoundTag compound = itemStack.getTagElement("BlockEntityTag");
         if (compound != null) {
            CompoundTag inventoryTag = compound.getCompound("Inventory");
            if (inventoryTag.contains("Items", 9)) {
               ItemStackHandler handler = new ItemStackHandler();
               handler.deserializeNBT(inventoryTag);
               ItemStack newDrinkStack = handler.getStackInSlot(2);
               newDrinkStack.shrink(1);
               compound.remove("Inventory");
               compound.put("Inventory", handler.serializeNBT());
            }
         }
      }
   }

   public static ItemStack getContainerFromItem(ItemStack itemStack) {
      if (!itemStack.is((Item)ODItems.JUICER.get())) {
         return ItemStack.EMPTY;
      } else {
         CompoundTag compound = itemStack.getTagElement("BlockEntityTag");
         return compound != null ? ItemStack.of(compound.getCompound("Container")) : ItemStack.EMPTY;
      }
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.inventory.deserializeNBT(compound.getCompound("Inventory"));
      this.juiceTime = compound.getInt("JuiceTime");
      this.juiceTimeTotal = compound.getInt("JuiceTimeTotal");
      this.drinkContainerStack = ItemStack.of(compound.getCompound("Container"));
      if (compound.contains("CustomName", 8)) {
         this.customName = Serializer.fromJson(compound.getString("CustomName"));
      }

      CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");
      Iterator var3 = compoundRecipes.getAllKeys().iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         this.usedRecipeTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
      }

   }

   public void saveAdditional(CompoundTag compound) {
      super.saveAdditional(compound);
      compound.putInt("JuiceTime", this.juiceTime);
      compound.putInt("JuiceTimeTotal", this.juiceTimeTotal);
      compound.put("Container", this.drinkContainerStack.serializeNBT());
      if (this.customName != null) {
         compound.putString("CustomName", Serializer.toJson(this.customName));
      }

      compound.put("Inventory", this.inventory.serializeNBT());
      CompoundTag compoundRecipes = new CompoundTag();
      this.usedRecipeTracker.forEach((recipeId, craftedAmount) -> {
         compoundRecipes.putInt(recipeId.toString(), craftedAmount);
      });
      compound.put("RecipesUsed", compoundRecipes);
   }

   private CompoundTag writeItems(CompoundTag compound) {
      super.saveAdditional(compound);
      compound.put("Container", this.drinkContainerStack.serializeNBT());
      compound.put("Inventory", this.inventory.serializeNBT());
      return compound;
   }

   public CompoundTag writeDrink(CompoundTag compound) {
      if (this.getDrink().isEmpty()) {
         return compound;
      } else {
         ItemStackHandler drops = new ItemStackHandler(5);

         for(int i = 0; i < 5; ++i) {
            drops.setStackInSlot(i, i == 2 ? this.inventory.getStackInSlot(i) : ItemStack.EMPTY);
         }

         if (this.customName != null) {
            compound.putString("CustomName", Serializer.toJson(this.customName));
         }

         compound.put("Container", this.drinkContainerStack.serializeNBT());
         compound.put("Inventory", drops.serializeNBT());
         return compound;
      }
   }

   public static void juicingTick(Level level, BlockPos pos, BlockState state, JuicerBlockEntity juicer) {
      boolean didInventoryChange = false;
      if (juicer.hasInput()) {
         Optional<JuicerRecipe> recipe = juicer.getMatchingRecipe(new RecipeWrapper(juicer.inventory));
         if (recipe.isPresent() && juicer.canJuice(recipe.get())) {
            didInventoryChange = juicer.processJuicing(recipe.get(), juicer);
         } else {
            juicer.juiceTime = 0;
         }
      } else if (juicer.juiceTimeTotal > 0) {
         juicer.juiceTime = Mth.clamp(juicer.juiceTime - 2, 0, juicer.juiceTimeTotal);
      }

      ItemStack drinkStack = juicer.getDrink();
      if (!drinkStack.isEmpty()) {
         if (!juicer.doesDrinkHaveContainer(drinkStack)) {
            juicer.moveDrinkToOutput();
            didInventoryChange = true;
         } else if (!juicer.inventory.getStackInSlot(3).isEmpty()) {
            juicer.useStoredContainersOnMeal();
            didInventoryChange = true;
         }
      }

      if (didInventoryChange) {
         juicer.inventoryChanged();
      }

   }

   public static void animationTick(Level level, BlockPos pos, BlockState state, JuicerBlockEntity juicer) {
   }

   private Optional<JuicerRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
      if (this.level == null) {
         return Optional.empty();
      } else {
         if (this.lastRecipeID != null) {
            Recipe<RecipeWrapper> recipe = (Recipe)((RecipeManagerAccessor)this.level.getRecipeManager()).getRecipeMap((RecipeType) ODRecipeTypes.JUICING.get()).get(this.lastRecipeID);
            if (recipe instanceof JuicerRecipe) {
               if (recipe.matches(inventoryWrapper, this.level)) {
                  return Optional.of((JuicerRecipe) recipe);
               }

               if (ItemStack.isSameItem(recipe.getResultItem(this.level.registryAccess()), this.getDrink())) {
                  return Optional.empty();
               }
            }
         }

         if (this.checkNewRecipe) {
            Optional<JuicerRecipe> recipe = this.level.getRecipeManager().getRecipeFor((RecipeType)ODRecipeTypes.JUICING.get(), inventoryWrapper, this.level);
            if (recipe.isPresent()) {
               ResourceLocation newRecipeID = (recipe.get()).getId();
               if (this.lastRecipeID != null && !this.lastRecipeID.equals(newRecipeID)) {
                  this.juiceTime = 0;
               }

               this.lastRecipeID = newRecipeID;
               return recipe;
            }
         }

         this.checkNewRecipe = false;
         return Optional.empty();
      }
   }

   public ItemStack getContainer() {
      ItemStack drinkStack = this.getDrink();
      return !drinkStack.isEmpty() && !this.drinkContainerStack.isEmpty() ? this.drinkContainerStack : drinkStack.getCraftingRemainingItem();
   }

   private boolean hasInput() {
      for(int i = 0; i < 2; ++i) {
         if (!this.inventory.getStackInSlot(i).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   protected boolean canJuice(JuicerRecipe recipe) {
      if (this.hasInput()) {
         ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
         if (resultStack.isEmpty()) {
            return false;
         } else {
            ItemStack storedDrinkStack = this.inventory.getStackInSlot(2);
            if (storedDrinkStack.isEmpty()) {
               return true;
            } else if (!ItemStack.isSameItem(storedDrinkStack, resultStack)) {
               return false;
            } else if (storedDrinkStack.getCount() + resultStack.getCount() <= this.inventory.getSlotLimit(2)) {
               return true;
            } else {
               return storedDrinkStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
            }
         }
      } else {
         return false;
      }
   }

   private boolean processJuicing(JuicerRecipe recipe, JuicerBlockEntity juicer) {
      if (this.level == null) {
         return false;
      } else {
         ++this.juiceTime;
         this.juiceTimeTotal = recipe.getJuiceTime();
         if (this.juiceTime < this.juiceTimeTotal) {
            return false;
         } else {
            this.juiceTime = 0;
            this.drinkContainerStack = recipe.getOutputContainer();
            ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
            ItemStack storedMealStack = this.inventory.getStackInSlot(2);
            if (storedMealStack.isEmpty()) {
               this.inventory.setStackInSlot(2, resultStack.copy());
            } else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
               storedMealStack.grow(resultStack.getCount());
            }

            juicer.setRecipeUsed(recipe);

            for(int i = 0; i < 2; ++i) {
               ItemStack slotStack = this.inventory.getStackInSlot(i);
               if (slotStack.hasCraftingRemainingItem()) {
                  Direction direction = this.getBlockState().getValue(JuicerBlock.FACING).getCounterClockWise();
                  double x = (double)this.getBlockPos().getX() + 0.5 + (double)direction.getStepX() * 0.25;
                  double y = (double)this.getBlockPos().getY() + 0.7;
                  double z = (double)this.getBlockPos().getZ() + 0.5 + (double)direction.getStepZ() * 0.25;
                  ItemUtils.spawnItemEntity(
                          this.level,
                          this.inventory.getStackInSlot(i).getCraftingRemainingItem(),
                          x,
                          y,
                          z,
                          (double)((float)direction.getStepX() * 0.08F),
                          0.25,
                          (double)((float)direction.getStepZ() * 0.08F)
                  );
               }

               if (!slotStack.isEmpty()) {
                  slotStack.shrink(1);
               }
            }
            return true;
         }
      }
   }

   public void setRecipeUsed(@Nullable Recipe<?> recipe) {
      if (recipe != null) {
         ResourceLocation recipeID = recipe.getId();
         this.usedRecipeTracker.addTo(recipeID, 1);
      }

   }

   @Nullable
   public Recipe<?> getRecipeUsed() {
      return null;
   }

   public void awardUsedRecipes(Player player, List<ItemStack> items) {
      List<Recipe<?>> usedRecipes = this.getUsedRecipesAndPopExperience(player.level(), player.position());
      player.awardRecipes(usedRecipes);
      this.usedRecipeTracker.clear();
   }

   public List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
      List<Recipe<?>> list = Lists.newArrayList();
      ObjectIterator var4 = this.usedRecipeTracker.object2IntEntrySet().iterator();

      while(var4.hasNext()) {
         Object2IntMap.Entry<ResourceLocation> entry = (Object2IntMap.Entry)var4.next();
         level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
            list.add(recipe);
            splitAndSpawnExperience((ServerLevel)level, pos, entry.getIntValue(), ((JuicerRecipe)recipe).getExperience());
         });
      }

      return list;
   }

   private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
      int expTotal = Mth.floor((float)craftedAmount * experience);
      float expFraction = Mth.frac((float)craftedAmount * experience);
      if (expFraction != 0.0F && Math.random() < (double)expFraction) {
         ++expTotal;
      }

      ExperienceOrb.award(level, pos, expTotal);
   }

   public ItemStackHandler getInventory() {
      return this.inventory;
   }

   public ItemStack getDrink() {
      return this.inventory.getStackInSlot(2);
   }

   public NonNullList<ItemStack> getDroppableInventory() {
      NonNullList<ItemStack> drops = NonNullList.create();

      for(int i = 0; i < 5; ++i) {
         if (i != 2) {
            drops.add(this.inventory.getStackInSlot(i));
         }
      }

      return drops;
   }

   private void moveDrinkToOutput() {
      ItemStack drinkStack = this.inventory.getStackInSlot(2);
      ItemStack outputStack = this.inventory.getStackInSlot(4);
      int mealCount = Math.min(drinkStack.getCount(), drinkStack.getMaxStackSize() - outputStack.getCount());
      if (outputStack.isEmpty()) {
         this.inventory.setStackInSlot(4, drinkStack.split(mealCount));
      } else if (outputStack.getItem() == drinkStack.getItem()) {
         drinkStack.shrink(mealCount);
         outputStack.grow(mealCount);
      }

   }

   private void useStoredContainersOnMeal() {
      ItemStack drinkStack = this.inventory.getStackInSlot(2);
      ItemStack containerInputStack = this.inventory.getStackInSlot(3);
      ItemStack outputStack = this.inventory.getStackInSlot(4);
      if (this.isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
         int smallerStackCount = Math.min(drinkStack.getCount(), containerInputStack.getCount());
         int drinkCount = Math.min(smallerStackCount, drinkStack.getMaxStackSize() - outputStack.getCount());
         if (outputStack.isEmpty()) {
            containerInputStack.shrink(drinkCount);
            this.inventory.setStackInSlot(4, drinkStack.split(drinkCount));
         } else if (outputStack.getItem() == drinkStack.getItem()) {
            drinkStack.shrink(drinkCount);
            containerInputStack.shrink(drinkCount);
            outputStack.grow(drinkCount);
         }
      }

   }

   public ItemStack useHeldItemOnDrink(ItemStack container) {
      if (this.isContainerValid(container) && !this.getDrink().isEmpty()) {
         container.shrink(1);
         return this.getDrink().split(1);
      } else {
         return ItemStack.EMPTY;
      }
   }

   private boolean doesDrinkHaveContainer(ItemStack meal) {
      return !this.drinkContainerStack.isEmpty() || meal.hasCraftingRemainingItem();
   }

   public boolean isContainerValid(ItemStack containerItem) {
      if (containerItem.isEmpty()) {
         return false;
      } else {
         return !this.drinkContainerStack.isEmpty() ? this.drinkContainerStack.is(containerItem.getItem()) : this.getDrink().getCraftingRemainingItem().is(containerItem.getItem());
      }
   }

   public Component getName() {
      return this.customName != null ? this.customName : Component.literal("Juicer");
   }

   public Component getDisplayName() {
      return this.getName();
   }

   @Nullable
   public Component getCustomName() {
      return this.customName;
   }

   public void setCustomName(Component name) {
      this.customName = name;
   }

   public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
      return new JuicerMenu(id, player, this, this.kegData);
   }

   @Nonnull
   public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
      if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
         return side != null && !side.equals(Direction.UP) ? this.outputHandler.cast() : this.inputHandler.cast();
      } else {
         return super.getCapability(cap, side);
      }
   }

   public void setRemoved() {
      super.setRemoved();
      this.inputHandler.invalidate();
      this.outputHandler.invalidate();
   }

   public CompoundTag getUpdateTag() {
      return this.writeItems(new CompoundTag());
   }

   private ItemStackHandler createHandler() {
      return new ItemStackHandler(5) {
         protected void onContentsChanged(int slot) {
            if (slot >= 0 && slot < 2) {
               JuicerBlockEntity.this.checkNewRecipe = true;
            }

            JuicerBlockEntity.this.inventoryChanged();
         }
      };
   }

   private ContainerData createIntArray() {
      return new ContainerData() {
         public int get(int index) {
            int var10000 = switch (index) {
                case 0 -> JuicerBlockEntity.this.juiceTime;
                case 1 -> JuicerBlockEntity.this.juiceTimeTotal;
                default -> 0;
            };
             return var10000;
         }

         public void set(int index, int value) {
            switch(index) {
            case 0 -> JuicerBlockEntity.this.juiceTime = value;
            case 1 -> JuicerBlockEntity.this.juiceTimeTotal = value;
            }
         }
         public int getCount() {
            return 2;
         }
      };
   }
}
